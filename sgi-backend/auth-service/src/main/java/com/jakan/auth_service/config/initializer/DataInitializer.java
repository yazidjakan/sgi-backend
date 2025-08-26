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
    public void run(String... args) {
        // --- Création des rôles si inexistants ---
        Role adminRole = roleDao.findByNom("ROLE_ADMIN")
                .orElseGet(() -> roleDao.save(new Role(null, "ROLE_ADMIN")));

        Role userRole = roleDao.findByNom("ROLE_USER")
                .orElseGet(() -> roleDao.save(new Role(null, "ROLE_USER")));

        Role techRole = roleDao.findByNom("ROLE_TECHNICIEN")
                .orElseGet(() -> roleDao.save(new Role(null, "ROLE_TECHNICIEN")));

        Role managerRole = roleDao.findByNom("ROLE_MANAGER")
                .orElseGet(() -> roleDao.save(new Role(null, "ROLE_MANAGER")));

        // --- Création des utilisateurs si inexistants ---
        if (userDao.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@dxc.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(new HashSet<>(Set.of(adminRole)));
            userDao.save(admin);
        }

        if (userDao.findByUsername("manager1").isEmpty()) {
            User manager = new User();
            manager.setUsername("manager1");
            manager.setEmail("manager1@dxc.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setRoles(new HashSet<>(Set.of(managerRole)));
            userDao.save(manager);
        }

        if (userDao.findByUsername("tech1").isEmpty()) {
            User tech = new User();
            tech.setUsername("tech1");
            tech.setEmail("tech1@dxc.com");
            tech.setPassword(passwordEncoder.encode("tech123"));
            tech.setRoles(new HashSet<>(Set.of(techRole)));
            userDao.save(tech);
        }

        if (userDao.findByUsername("user1").isEmpty()) {
            User user = new User();
            user.setUsername("user1");
            user.setEmail("user1@dxc.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRoles(new HashSet<>(Set.of(userRole)));
            userDao.save(user);
        }
    }
}