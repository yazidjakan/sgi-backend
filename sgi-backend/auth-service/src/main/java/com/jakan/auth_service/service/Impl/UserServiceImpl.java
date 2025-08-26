package com.jakan.auth_service.service.Impl;

import com.jakan.auth_service.dto.RoleDto;
import com.jakan.auth_service.dto.UserDto;
import com.jakan.auth_service.entity.Role;
import com.jakan.auth_service.entity.User;
import com.jakan.auth_service.repository.RoleRepository;
import com.jakan.auth_service.repository.UserRepository;
import com.jakan.auth_service.service.facade.UserService;
import com.jakan.auth_service.transformer.RoleTransformer;
import com.jakan.auth_service.transformer.UserTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserTransformer userTransformer;
    private final UserRepository userDao;
    private final RoleTransformer roleTransformer;
    private final RoleRepository roleDao;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDto findById(Long id) {
        log.info("Fetching user by ID");
        return userDao.findById(id)
                .map(userTransformer::toDto)
                .orElseThrow(() ->{
                        log.error("User not found with ID: {}"+id);
                return new RuntimeException("Unable to find a User with the ID: "+id);
                });
    }

    @Override
    public List<UserDto> findAll() {
        log.info("Fetching all users");
        List<User> users=userDao.findAll();
        if(users.isEmpty()){
            throw new RuntimeException("List of users is Empty");
        }
        return userTransformer.toDto(users);
    }

    private UserDto prepareUser(UserDto dto){
        List<User> usersRegistred=userDao.findAll();
        long nbreAdmins=usersRegistred.stream()
                .map(user -> user.getRoles().equals("ROLE_ADMIN"))
                .count();
        if(nbreAdmins !=0 && dto.roleDtos().equals("ROLE_ADMIN")){
            throw new RuntimeException("Il peut y'avoir qu'un seul Admin");
        }else{
            return dto;
        }
    }

    @Override
    public UserDto save(UserDto dto) {
        log.info("Attempting to create user with username: {}", dto.username());
        if (dto.username() != null) {
            boolean userExists = userDao.findByUsername(dto.username()).isPresent();
            if (userExists) {
                throw new RuntimeException("This username is already used");
            }
        }
        if (dto.roleDtos() == null || dto.roleDtos().isEmpty()) {
            throw new RuntimeException("User must have at least one role");
        }
        try {
            log.info("Roles reçus: {}", dto.roleDtos()); // Ajoutez ce log pour vérifier les rôles reçus

            User user = userTransformer.toEntity(dto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Set<Role> userRoles = new HashSet<>();
            for (RoleDto roleDto : dto.roleDtos()) {
                Optional<Role> roleOptional = roleDao.findByNom(roleDto.nom());
                if (roleOptional.isPresent()) {
                    userRoles.add(roleOptional.get());
                } else {
                    Role newRole = new Role();
                    newRole.setNom(roleDto.nom());
                    newRole = roleDao.save(newRole);
                    userRoles.add(newRole);
                }
            }
            user.setRoles(userRoles);
            log.info("User created successfully with username: {}", dto.username());
            return userTransformer.toDto(userDao.save(user));
        } catch (Exception ex) {
            log.error("Error occurred while creating user with username: {}", dto.username(), ex);
            throw new RuntimeException("An unexpected error occurred while creating the User." + ex);
        }
    }

    @Override
    public UserDto update(UserDto dto, Long id) {
        log.info("Updating user with ID: {}", id);

        // Vérifie si l'utilisateur existe
        User existingUser = userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Mets à jour les propriétés de l'utilisateur
        existingUser.setUsername(dto.username());
        existingUser.setPassword(dto.password());
        existingUser.setEmail(dto.email());

        // Gère les rôles associés
        existingUser.getRoles().clear();
        existingUser.getRoles().addAll(roleTransformer.toEntitySet(dto.roleDtos()));

        // Sauvegarde l'utilisateur mis à jour
        User updatedUser = userDao.save(existingUser);

        log.info("User updated successfully with ID: {}", id);
        return userTransformer.toDto(updatedUser);
    }

    @Override
    public void deleteById(Long id) {

        log.info("Deleting user with ID: {}", id);

        UserDto foundUser = findById(id);
        foundUser.roleDtos().clear();
        userDao.deleteById(foundUser.id());
        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    public UserDto findByUsername(String username) {
        log.info("Fetching user by username: {}", username);

        return userDao.findByUsername(username)
                .map(userTransformer::toDto)
                .orElseThrow(() -> {
                    log.error("User not found with username {}", username);
                    return new RuntimeException("Unable to find user with the username :"+username);
                });
    }
}
