package com.sagar.sms.controller;

import com.sagar.sms.dto.CourseReportDTO;
import com.sagar.sms.dto.StudentReportDTO;
import com.sagar.sms.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentReportDTO> getStudentReport(
            @PathVariable Long studentId) {

        return ResponseEntity.ok(
                enrollmentService.getStudentReport(studentId)
        );
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<CourseReportDTO> getCourseReport(
            @PathVariable Long courseId) {

        return ResponseEntity.ok(
                enrollmentService.getCourseReport(courseId)
        );
    }
}