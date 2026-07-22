package com.sagar.sms.dto;

import lombok.Data;

@Data
public class StudentCourseGradeDTO {

    private String courseName;

    private Double grade;

    private String remarks;
}