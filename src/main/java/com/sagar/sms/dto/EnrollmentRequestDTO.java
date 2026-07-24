package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Enrollment Request",
        description = "Request payload for enrolling a student into a course"
)
public class EnrollmentRequestDTO {

    @Schema(
            description = "Unique identifier of the student",
            example = "1"
    )
    @NotNull(message = "Student id is required")
    private Long studentId;

    @Schema(
            description = "Unique identifier of the course",
            example = "2"
    )
    @NotNull(message = "Course id is required")
    private Long courseId;

    @Schema(
            description = "Date on which the student is enrolled",
            example = "2026-07-20"
    )
    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @Schema(
            description = "Current enrollment status",
            example = "ACTIVE",
            allowableValues = {"ACTIVE", "PENDING", "COMPLETED", "CANCELLED"}
    )
    @NotBlank(message = "Status is required")
    private String status;
}