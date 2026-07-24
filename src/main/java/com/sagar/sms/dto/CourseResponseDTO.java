package com.sagar.sms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "Course Response",
        description = "Response object containing course details"
)
public class CourseResponseDTO {

    @Schema(
            description = "Unique course identifier",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Name of the course",
            example = "Java Programming"
    )
    private String courseName;

    @Schema(
            description = "Unique course code",
            example = "JAVA101"
    )
    private String courseCode;

    @Schema(
            description = "Brief description of the course",
            example = "Comprehensive course covering Core Java, OOP concepts, Collections, Exception Handling, Multithreading, and Java 8 features."
    )
    private String description;

    @Schema(
            description = "Date and time when the course was created",
            example = "2026-07-20T10:15:30"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the course was last updated",
            example = "2026-07-22T18:45:10"
    )
    private LocalDateTime updatedAt;
}