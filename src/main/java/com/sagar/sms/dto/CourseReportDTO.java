package com.sagar.sms.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseReportDTO {

    private Long courseId;

    private String courseName;

    private Integer totalStudents;

    private Double averageGrade;

    private List<CourseStudentGradeDTO> students;
}