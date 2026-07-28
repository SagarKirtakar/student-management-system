package com.sagar.sms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Swagger
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Student APIs
                        .requestMatchers("/students/**")
                        .hasRole("ADMIN")

                        // Course APIs
                        .requestMatchers("/courses/**")
                        .hasRole("ADMIN")

                        // Enrollment APIs
                        .requestMatchers("/enrollments/**")
                        .hasRole("ADMIN")

                        // Report APIs
                        .requestMatchers("/reports/**")
                        .hasAnyRole("ADMIN", "TEACHER")

                        // Everything else
                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin123"))
//                .roles("ADMIN")
//                .build();
//
//        UserDetails teacher = User.builder()
//                .username("teacher")
//                .password(passwordEncoder().encode("teacher123"))
//                .roles("TEACHER")
//                .build();
//
//        UserDetails student = User.builder()
//                .username("student")
//                .password(passwordEncoder().encode("student123"))
//                .roles("STUDENT")
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                admin,
//                teacher,
//                student
//        );
//    }

}
