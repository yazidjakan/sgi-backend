package com.jakan.auth_service.config.initializer;


import com.jakan.auth_service.entity.Role;
import com.jakan.auth_service.entity.User;
import com.jakan.auth_service.repository.RoleRepository;
import com.jakan.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userDao;
    private final RoleRepository roleDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Role adminRole;
        Role userRole;
        Role techRole;
        Role managerRole;

        var roleOpt = roleDao.findByNom("ROLE_ADMIN");
        var roleUs=roleDao.findByNom("ROLE_USER");
        var roleTech=roleDao.findByNom("ROLE_TECHNICIEN");
        var roleManager=roleDao.findByNom("ROLE_MANAGER");
        if (roleOpt.isPresent()) {
            adminRole = roleOpt.get();
        } else {
            adminRole = new Role();
            adminRole.setNom("ROLE_ADMIN");
            adminRole = roleDao.save(adminRole);
        }

        if (userDao.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@dxc.com");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);

            userDao.save(admin);
        }

        if(roleDao.findByNom("ROLE_USER").isEmpty()){
            userRole=new Role();
            userRole.setNom("ROLE_USER");
            roleDao.save(userRole);
        }

        if(roleDao.findByNom("ROLE_TECHNICIEN").isEmpty()){
            techRole=new Role();
            techRole.setNom("ROLE_TECHNICIEN");
            roleDao.save(techRole);
        }
        if(roleDao.findByNom("ROLE_MANAGER").isEmpty()){
            managerRole=new Role();
            managerRole.setNom("ROLE_MANAGER");
            roleDao.save(managerRole);
        }
    }
}
