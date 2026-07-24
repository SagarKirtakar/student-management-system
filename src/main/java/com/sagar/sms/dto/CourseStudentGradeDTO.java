package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Course Student Grade",
        description = "Student details, grade, and remarks for a course report"
)
public class CourseStudentGradeDTO {

    @Schema(
            description = "Full name of the student",
            example = "Rahul Sharma"
    )
    private String studentName;

    @Schema(
            description = "Grade obtained by the student in the course",
            example = "92",
            minimum = "0",
            maximum = "100"
    )
    private Double grade;

    @Schema(
            description = "Instructor's remarks for the student's performance",
            example = "Outstanding"
    )
    private String remarks;
}