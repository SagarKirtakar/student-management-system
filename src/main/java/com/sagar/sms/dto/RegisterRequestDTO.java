package com.sagar.sms.dto;

import com.sagar.sms.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._]{3,20}$",
            message = "Username must be 3-20 characters and contain only letters, numbers, dots or underscores"
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;
}