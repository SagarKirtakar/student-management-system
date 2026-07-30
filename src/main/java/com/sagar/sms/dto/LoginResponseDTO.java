package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JWT authentication response")
public class LoginResponseDTO {

    @Schema(
            description = "JWT access token used to authenticate protected APIs",
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcyMjMxMjAwMCwiZXhwIjoxNzIyMzE1NjAwfQ.dummySignature"
    )
    private String token;
}