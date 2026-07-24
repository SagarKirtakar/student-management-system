package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "Student Report",
        description = "Detailed report containing a student's enrolled courses, grades, and overall performance"
)
public class StudentReportDTO {

    @Schema(
            description = "Unique student identifier",
            example = "2"
    )
    private Long studentId;

    @Schema(
            description = "Full name of the student",
            example = "Rahul Sharma"
    )
    private String studentName;

    @Schema(
            description = "List of courses enrolled by the student along with grades and remarks"
    )
    private List<StudentCourseGradeDTO> courses;

    @Schema(
            description = "Average grade obtained across all enrolled courses",
            example = "68.5",
            minimum = "0",
            maximum = "100"
    )
    private Double averageGrade;
}