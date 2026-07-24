package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "Course Request",
        description = "Request payload for creating or updating a course"
)
public class CourseRequestDTO {

    @Schema(
            description = "Name of the course",
            example = "Java Programming"
    )
    @NotBlank(message = "Course name is required")
    @Size(max = 255, message = "Course name must be under 255 characters")
    private String courseName;

    @Schema(
            description = "Unique course code",
            example = "JAVA101"
    )
    @NotBlank(message = "Course code is required")
    @Size(max = 50, message = "Course code must be under 50 characters")
    private String courseCode;

    @Schema(
            description = "Brief description of the course",
            example = "Comprehensive course covering Core Java, OOP concepts, Collections, Exception Handling, Multithreading, and Java 8 features."
    )
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}