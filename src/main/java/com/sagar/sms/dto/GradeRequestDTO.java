package com.sagar.sms.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradeRequestDTO {

    @NotNull(message = "Grade is required")
    @DecimalMin(value = "0.0", message = "Grade cannot be less than 0")
    @DecimalMax(value = "100.0", message = "Grade cannot be greater than 100")
    private Double grade;

    private String remarks;
}