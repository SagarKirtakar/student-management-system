package com.sagar.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponseDTO {

    private Long id;
    private String courseName;
    private String courseCode;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
