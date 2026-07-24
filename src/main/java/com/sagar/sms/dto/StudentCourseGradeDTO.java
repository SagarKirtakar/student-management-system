package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(
        name = "Student Course Grade",
        description = "Course details, grade, and remarks for a student's report"
)
public class StudentCourseGradeDTO {

    @Schema(
            description = "Name of the enrolled course",
            example = "Data Structures and Algorithms"
    )
    private String courseName;

    @Schema(
            description = "Grade obtained in the course",
            example = "92",
            minimum = "0",
            maximum = "100"
    )
    private Double grade;

    @Schema(
            description = "Instructor's remarks for the course",
            example = "Outstanding"
    )
    private String remarks;
}