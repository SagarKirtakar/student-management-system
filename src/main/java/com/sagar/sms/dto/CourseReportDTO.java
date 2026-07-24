package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(
        name = "Course Report",
        description = "Detailed report containing course information, enrolled students, and overall performance statistics"
)
public class CourseReportDTO {

    @Schema(
            description = "Unique course identifier",
            example = "2"
    )
    private Long courseId;

    @Schema(
            description = "Name of the course",
            example = "Data Structures and Algorithms"
    )
    private String courseName;

    @Schema(
            description = "Total number of students enrolled in the course",
            example = "25"
    )
    private Integer totalStudents;

    @Schema(
            description = "Average grade of all enrolled students",
            example = "84.6",
            minimum = "0",
            maximum = "100"
    )
    private Double averageGrade;

    @Schema(
            description = "List of students enrolled in the course along with their grades and remarks"
    )
    private List<CourseStudentGradeDTO> students;
}