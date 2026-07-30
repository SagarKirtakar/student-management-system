package com.sagar.sms.services;

import com.sagar.sms.dto.LoginRequestDTO;
import com.sagar.sms.dto.LoginResponseDTO;
import com.sagar.sms.dto.RegisterRequestDTO;
import com.sagar.sms.dto.UserResponseDTO;

public interface UserService {

    public UserResponseDTO register(RegisterRequestDTO request);

    public LoginResponseDTO verify(LoginRequestDTO request);

}
