package com.sagar.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {

    private Long id;

    private Long studentId;
    private String studentName;

    private Long courseId;
    private String courseName;

    private LocalDate enrollmentDate;

    private String status;

    private Double grade;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}