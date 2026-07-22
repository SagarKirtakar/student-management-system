package com.sagar.sms.services;

import com.sagar.sms.dto.*;
import com.sagar.sms.entity.Enrollment;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EnrollmentService {

    void createEnrollment(EnrollmentRequestDTO requestDTO);

    List<EnrollmentResponseDTO> getAllEnrollments();

    EnrollmentResponseDTO getEnrollmentById(long id);

    void updateEnrollmentById(long id, EnrollmentRequestDTO requestDTO);

    void deleteEnrollmentById(long id);

    Page<EnrollmentResponseDTO> getEnrollments(Pageable pageable);

    Page<EnrollmentResponseDTO> searchEnrollments(String status, Pageable pageable);

    void assignGrade(Long enrollmentId, GradeRequestDTO requestDTO);

    StudentReportDTO getStudentReport(Long studentId);

    CourseReportDTO getCourseReport(Long courseId);

}
