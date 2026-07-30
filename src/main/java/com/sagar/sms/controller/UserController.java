package com.sagar.sms.controller;

import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;
import com.sagar.sms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and authentication")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Register User",
            description = "Registers a new user with an encoded password and assigned role."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid
            @RequestBody
            @Parameter(description = "User registration details")
            RegisterRequestDTO request) {

        return userService.register(request);
    }

    @Operation(
            summary = "User Login",
            description = "Authenticates the user and returns a JWT token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid
            @RequestBody
            @Parameter(description = "User login credentials")
            LoginRequestDTO request) {

        return userService.verify(request);
    }

}