package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Student Response",
        description = "Response object containing student details"
)
public class StudentResponseDTO {

    @Schema(
            description = "Unique student identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Student's first name",
            example = "Sagar"
    )
    private String firstName;

    @Schema(
            description = "Student's last name",
            example = "Kirtakar"
    )
    private String lastName;

    @Schema(
            description = "Student's email address",
            example = "sagar@gmail.com"
    )
    private String email;

    @Schema(
            description = "Student's mobile number",
            example = "9876543210"
    )
    private String phone;

    @Schema(
            description = "Student's date of birth",
            example = "2002-08-15"
    )
    private LocalDate dateOfBirth;

    @Schema(
            description = "Date and time when the student record was created",
            example = "2026-07-20T10:15:30"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the student record was last updated",
            example = "2026-07-22T18:45:10"
    )
    private LocalDateTime updatedAt;

}