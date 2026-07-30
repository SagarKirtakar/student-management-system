package com.sagar.sms.dto;

import com.sagar.sms.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User registration response")
public class UserResponseDTO {

    @Schema(
            description = "Unique identifier of the user",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Username of the registered user",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Role assigned to the user",
            example = "ADMIN",
            allowableValues = {"ADMIN", "TEACHER", "STUDENT"}
    )
    private Role role;
}