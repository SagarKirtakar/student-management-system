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
        name = "Enrollment Response",
        description = "Response object containing enrollment details"
)
public class EnrollmentResponseDTO {

    @Schema(
            description = "Unique enrollment identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Unique identifier of the enrolled student",
            example = "2"
    )
    private Long studentId;

    @Schema(
            description = "Full name of the enrolled student",
            example = "Rahul Sharma"
    )
    private String studentName;

    @Schema(
            description = "Unique identifier of the course",
            example = "3"
    )
    private Long courseId;

    @Schema(
            description = "Name of the enrolled course",
            example = "Object-Oriented Programming with Java"
    )
    private String courseName;

    @Schema(
            description = "Date when the student enrolled in the course",
            example = "2026-07-20"
    )
    private LocalDate enrollmentDate;

    @Schema(
            description = "Current enrollment status",
            example = "ACTIVE",
            allowableValues = {
                    "ACTIVE",
                    "PENDING",
                    "COMPLETED",
                    "CANCELLED"
            }
    )
    private String status;

    @Schema(
            description = "Student's grade in the course",
            example = "92",
            minimum = "0",
            maximum = "100"
    )
    private Double grade;

    @Schema(
            description = "Instructor's remarks about the student's performance",
            example = "Outstanding"
    )
    private String remarks;

    @Schema(
            description = "Date and time when the enrollment record was created",
            example = "2026-07-20T11:15:57"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the enrollment record was last updated",
            example = "2026-07-22T19:58:31"
    )
    private LocalDateTime updatedAt;
}