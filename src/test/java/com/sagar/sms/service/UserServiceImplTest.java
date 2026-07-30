package com.sagar.sms.services;

import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;
import com.sagar.sms.entity.Role;
import com.sagar.sms.entity.Users;
import com.sagar.sms.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private UserRepo repo;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private Users user;

    @BeforeEach
    void setUp() {

        registerRequest = RegisterRequestDTO.builder()
                .username("admin")
                .password("Admin@123")
                .role(Role.ADMIN)
                .build();

        loginRequest = LoginRequestDTO.builder()
                .username("admin")
                .password("Admin@123")
                .build();

        user = Users.builder()
                .id(1L)
                .username("admin")
                .password("encodedPassword")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void register_ShouldReturnUserResponse_WhenRequestIsValid() {

        when(passwordEncoder.encode(registerRequest.getPassword()))
                .thenReturn("encodedPassword");

        when(repo.save(any(Users.class)))
                .thenReturn(user);

        UserResponseDTO response = userService.register(registerRequest);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("admin", response.getUsername());
        assertEquals(Role.ADMIN, response.getRole());

        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(repo).save(any(Users.class));
    }

    @Test
    void verify_ShouldReturnJwtToken_WhenAuthenticationIsSuccessful() {

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.isAuthenticated())
                .thenReturn(true);

        when(jwtService.generateToken("admin"))
                .thenReturn("jwt-token");

        LoginResponseDTO response = userService.verify(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken("admin");
    }

    @Test
    void verify_ShouldThrowException_WhenAuthenticationFails() {

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.isAuthenticated())
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.verify(loginRequest)
        );

        assertEquals("Invalid username or password", exception.getMessage());

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(anyString());
    }
}


