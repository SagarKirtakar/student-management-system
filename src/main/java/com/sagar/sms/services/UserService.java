package com.sagar.sms.services;

import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;
import com.sagar.sms.entity.Users;
import com.sagar.sms.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JWTService jwtService;

    private final AuthenticationManager authManager;

    private final UserRepo repo;

    private final BCryptPasswordEncoder passwordEncoder;


    public UserResponseDTO register(RegisterRequestDTO request) {

        Users user = Users.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        Users savedUser = repo.save(user);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }


    public LoginResponseDTO verify(LoginRequestDTO request) {

        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        if (authentication.isAuthenticated()) {
            return LoginResponseDTO.builder()
                    .token(jwtService.generateToken(request.getUsername()))
                    .build();
        }

        throw new RuntimeException("Invalid username or password");
    }

}