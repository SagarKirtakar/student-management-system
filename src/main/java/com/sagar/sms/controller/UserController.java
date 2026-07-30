package com.sagar.sms.controller;


import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;
import com.sagar.sms.entity.Users;
import com.sagar.sms.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public UserResponseDTO register(
            @Valid @RequestBody RegisterRequestDTO request) {

        return service.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody LoginRequestDTO request) {

        return service.verify(request);
    }

}