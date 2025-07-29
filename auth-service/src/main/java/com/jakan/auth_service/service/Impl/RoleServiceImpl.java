package com.jakan.auth_service.service.Impl;

import com.jakan.auth_service.dto.RoleDto;
import com.jakan.auth_service.entity.Role;
import com.jakan.auth_service.repository.RoleRepository;
import com.jakan.auth_service.service.facade.RoleService;
import com.jakan.auth_service.transformer.RoleTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleDao;
    private final RoleTransformer roleTransformer;

    @Override
    public RoleDto findById(Long id) {
        log.info("Fetching role by ID: {}", id);
        return roleDao.findById(id)
                .map(roleTransformer::toDto)
                .orElseThrow(() ->{
                    log.error("Role not found with ID: {}", id);
                    return new RuntimeException("Unable to find a role with the given Id : "+id);
                });
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles=roleDao.findAll();
        if(roles.isEmpty()){
            throw new RuntimeException("List of roles is Empty");
        }
        return roleTransformer.toDto(roles);
    }

    @Override
    public RoleDto save(RoleDto dto) {
        log.info("Creating new role with name: {}", dto.nom());

        if(roleDao.findByNom(dto.nom()).isPresent())
        {
            log.warn("Attempted to create a duplicate role with name: {}", dto.nom());
            throw new RuntimeException("This role name already exists");
        }
        try {
            Role role = roleTransformer.toEntity(dto);
            log.info("Successfully created role with name: {}", dto.nom());
            return roleTransformer.toDto(roleDao.save(role));

        }catch (Exception ex)
        {
            log.error("Error occurred while creating role with name: {}", dto.nom(), ex);
            throw new RuntimeException("An unexpected error occurred while creating the role."+ ex);
        }
    }

    @Override
    public RoleDto update(RoleDto dto, Long id) {
        id = dto.id();
        RoleDto existingRoleDto = findById(id);
        Role existingRole = roleTransformer.toEntity(dto);
        existingRole.setId(dto.id());
        existingRole.setNom(dto.nom());

        log.info("Successfully updated role with ID: {}", dto.id());
        return roleTransformer.toDto(roleDao.save(existingRole));
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting role with ID: {}", id);
        RoleDto foundRole = findById(id);
        roleDao.deleteById(foundRole.id());

        log.info("Successfully deleted role with ID: {}", id);
    }
}
