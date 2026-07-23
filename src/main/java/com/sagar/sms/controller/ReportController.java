package com.sagar.sms.controller;

import com.sagar.sms.dto.CourseReportDTO;
import com.sagar.sms.dto.StudentReportDTO;
import com.sagar.sms.services.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reports", description = "APIs for student and course reports")
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Get Student Report", description = "Generates a detailed report for a student, including enrolled courses, grades, remarks, and average grade.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student report generated successfully"), @ApiResponse(responseCode = "404", description = "Student not found")})
    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentReportDTO> getStudentReport(

            @Parameter(description = "Student ID", example = "1") @PathVariable Long studentId) {

        return ResponseEntity.ok(enrollmentService.getStudentReport(studentId));
    }

    @Operation(summary = "Get Course Report", description = "Generates a detailed report for a course, including enrolled students, grades, remarks, and average grade.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Course report generated successfully"), @ApiResponse(responseCode = "404", description = "Course not found")})
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseReportDTO> getCourseReport(

            @Parameter(description = "Course ID", example = "1") @PathVariable Long courseId) {

        return ResponseEntity.ok(enrollmentService.getCourseReport(courseId));
    }
}