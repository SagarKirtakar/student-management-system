package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Student Request",
        description = "Request payload for creating or updating a student"
)
public class StudentRequestDTO {

    @Schema(
            description = "Student's first name",
            example = "Sagar"
    )
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(
            description = "Student's last name",
            example = "Kirtakar"
    )
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(
            description = "Student's email address",
            example = "sagar@gmail.com"
    )
    @NotNull
    @Email(message = "Invalid email format")
    private String email;

    @Schema(
            description = "Student's mobile number",
            example = "9876543210"
    )
    @Pattern(
            regexp = "^(\\+91|91)?[0-9]{10}$",
            message = "Phone number must be 10 digits, optional +91 or 91 allowed"
    )
    private String phone;

    @Schema(
            description = "Student's date of birth",
            example = "2002-08-15"
    )
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}