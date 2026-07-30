package com.sagar.sms.dto;

import com.sagar.sms.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;

    private String username;

    private Role role;
}