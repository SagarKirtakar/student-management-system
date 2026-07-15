package com.sagar.sms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequestDTO {

    @NotBlank(message = "Course name is required")
    @Size(max = 255, message = "Course name must be under 255 characters")
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Size(max = 50, message = "Course code must be under 50 characters")
    private String courseCode;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}