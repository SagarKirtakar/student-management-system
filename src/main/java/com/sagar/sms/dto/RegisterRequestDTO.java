package com.sagar.sms.dto;

import com.sagar.sms.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User registration request")
public class RegisterRequestDTO {

    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._]{3,20}$",
            message = "Username must be 3-20 characters and contain only letters, numbers, dots or underscores"
    )
    @Schema(
            description = "Unique username for the user account",
            example = "admin",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character"
    )
    @Schema(
            description = "Strong password containing uppercase, lowercase, number, and special character",
            example = "Admin@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;

    @NotNull(message = "Role is required")
    @Schema(
            description = "Role assigned to the user",
            example = "ADMIN",
            allowableValues = {"ADMIN", "TEACHER", "STUDENT"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Role role;
}