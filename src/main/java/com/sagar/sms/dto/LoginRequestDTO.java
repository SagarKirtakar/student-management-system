package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User login request")
public class LoginRequestDTO {

    @NotBlank(message = "Username is required")
    @Schema(
            description = "Registered username",
            example = "admin",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String username;

    @NotBlank(message = "Password is required")
    @Schema(
            description = "User account password",
            example = "Admin@123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String password;
}