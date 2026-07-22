package com.sagar.sms.dto;

import lombok.Data;

import java.util.List;

@Data
public class StudentReportDTO {

    private Long studentId;

    private String studentName;

    private List<StudentCourseGradeDTO> courses;

    private Double averageGrade;
}