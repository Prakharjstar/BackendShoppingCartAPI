package com.dailycodework.demo.data;

import com.dailycodework.demo.Repositories.RoleRepository;
import com.dailycodework.demo.Repositories.UserRepository;
import com.dailycodework.demo.model.Role;
import com.dailycodework.demo.model.User;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Transactional
@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {


        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExists(defaultRoles);


        createDefaultUserIfNotExists();


        createDefaultAdminIfNotExists();
    }

    private void createDefaultRoleIfNotExists(Set<String> roles) {

        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .map(Role::new)
                .forEach(roleRepository::save);

        System.out.println("Default roles created (if they didn't already exist).");
    }

    private void createDefaultUserIfNotExists() {

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        for (int i = 1; i <= 5; i++) {

            String defaultEmail = "user" + i + "@gmail.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));

            userRepository.save(user);

            System.out.println("Default user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExists() {

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        for (int i = 1; i <= 2; i++) {

            String defaultEmail = "admin" + i + "@gmail.com";

            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }

            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));

            userRepository.save(user);

            System.out.println("Default admin user " + i + " created successfully.");
        }
    }
}