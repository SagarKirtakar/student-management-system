package com.sagar.sms.config;

import com.sagar.sms.entity.Role;
import com.sagar.sms.entity.User;
import com.sagar.sms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createUser("admin", "admin123", Role.ADMIN);

        createUser("teacher", "teacher123", Role.TEACHER);

        createUser("student", "student123", Role.STUDENT);
    }

    private void createUser(String username,
                            String password,
                            Role role) {

        if (userRepository.existsByUsername(username)) {
            return;
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .enabled(true)
                .build();

        userRepository.save(user);
    }
}