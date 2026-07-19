package com.sagar.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequestDTO {

    @NotNull(message = "Student id is required")
    private Long studentId;

    @NotNull(message = "Course id is required")
    private Long courseId;

    @NotNull(message = "Enrollment date is required")
    private LocalDate enrollmentDate;

    @NotBlank(message = "Status is required")
    private String status;
}