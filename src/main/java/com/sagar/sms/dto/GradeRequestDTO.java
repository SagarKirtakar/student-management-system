package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(
        name = "Grade Request",
        description = "Request payload for assigning or updating a student's grade"
)
public class GradeRequestDTO {

    @Schema(
            description = "Student's grade for the enrolled course",
            example = "92",
            minimum = "0",
            maximum = "100"
    )
    @NotNull(message = "Grade is required")
    @DecimalMin(value = "0.0", message = "Grade cannot be less than 0")
    @DecimalMax(value = "100.0", message = "Grade cannot be greater than 100")
    private Double grade;

    @Schema(
            description = "Instructor's remarks or feedback for the student",
            example = "Outstanding performance"
    )
    private String remarks;
}