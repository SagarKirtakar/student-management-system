package com.sagar.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagar.sms.config.JwtFilter;
import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;
import com.sagar.sms.entity.Role;
import com.sagar.sms.services.JWTService;
import com.sagar.sms.services.MyUserDetailsService;
import com.sagar.sms.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private MyUserDetailsService userDetailsService;

    @Test
    void register_ShouldReturnOk_WhenRequestIsValid() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("admin")
                .password("Admin@123")
                .role(Role.ADMIN)
                .build();

        UserResponseDTO response = UserResponseDTO.builder()
                .id(1L)
                .username("admin")
                .role(Role.ADMIN)
                .build();

        when(userService.register(any(RegisterRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));

        verify(userService).register(any(RegisterRequestDTO.class));
    }

    @Test
    void register_ShouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("")
                .password("Admin@123")
                .role(Role.ADMIN)
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).register(any());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenPasswordIsInvalid() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("admin")
                .password("123")
                .role(Role.ADMIN)
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).register(any());
    }

    @Test
    void register_ShouldReturnBadRequest_WhenRoleIsNull() throws Exception {

        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("admin")
                .password("Admin@123")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).register(any());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("admin")
                .password("Admin@123")
                .build();

        LoginResponseDTO response = LoginResponseDTO.builder()
                .token("jwt-token")
                .build();

        when(userService.verify(any(LoginRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));

        verify(userService).verify(any(LoginRequestDTO.class));
    }

    @Test
    void login_ShouldReturnBadRequest_WhenUsernameIsBlank() throws Exception {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("")
                .password("Admin@123")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).verify(any());
    }

    @Test
    void login_ShouldReturnBadRequest_WhenPasswordIsBlank() throws Exception {

        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("admin")
                .password("")
                .build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).verify(any());
    }
}

