package com.sagar.sms.services;

import com.sagar.sms.dto.*;
import com.sagar.sms.entity.Course;
import com.sagar.sms.entity.Enrollment;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.CourseNotFoundException;
import com.sagar.sms.exception.EnrollmentNotFoundException;
import com.sagar.sms.exception.StudentNotFoundException;
import com.sagar.sms.repository.CourseRepository;
import com.sagar.sms.repository.EnrollmentRepository;
import com.sagar.sms.repository.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    @Override
    public void createEnrollment(EnrollmentRequestDTO requestDTO) {

        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found : " + requestDTO.getStudentId()));

        Course course = courseRepository.findById(requestDTO.getCourseId())
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found : " + requestDTO.getCourseId()));

        Enrollment enrollment = new Enrollment();

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(requestDTO.getEnrollmentDate());
        enrollment.setStatus(requestDTO.getStatus());
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    @Transactional
    @Override
    public List<EnrollmentResponseDTO> getAllEnrollments() {

        return enrollmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    @Transactional
    @Override
    public EnrollmentResponseDTO getEnrollmentById(long id) {
        if (id <= 0) {
            throw new RuntimeException("Other type of exception");
        }

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException("Enrollment not found : "+id));

        return mapToResponseDTO(enrollment);
    }

    @Transactional
    @Override
    public void updateEnrollmentById(long id, EnrollmentRequestDTO requestDTO) {
        if (id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException("Enrollment not found : "+id));

        Student student = studentRepository.findById(requestDTO.getStudentId())
                .orElseThrow(() ->
                        new StudentNotFoundException("Student not found : "+requestDTO.getStudentId()));

        Course course = courseRepository.findById(requestDTO.getCourseId())
                .orElseThrow(() ->
                        new CourseNotFoundException("Course not found : "+requestDTO.getCourseId()));

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setStatus(requestDTO.getStatus());
        enrollment.setUpdatedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    @Transactional
    @Override
    public void deleteEnrollmentById(long id) {

        if (id <= 0) {
            throw new RuntimeException("Other type of exception");
        }

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException("Enrollment not found : " + id));

        enrollmentRepository.deleteById(enrollment.getId());
    }

    @Transactional
    @Override
    public Page<EnrollmentResponseDTO> getEnrollments(Pageable pageable) {

        Page<Enrollment> enrollmentPage = enrollmentRepository.findAll(pageable);

        return enrollmentPage.map(this::mapToResponseDTO);
    }

    @Transactional
    @Override
    public Page<EnrollmentResponseDTO> searchEnrollments(
            String status,
            Pageable pageable) {

        Page<Enrollment> enrollmentPage;

        if (status == null || status.isBlank()) {
            enrollmentPage = enrollmentRepository.findAll(pageable);
        } else {
            enrollmentPage = enrollmentRepository
                    .findByStatusContainingIgnoreCase(status, pageable);
        }

        return enrollmentPage.map(this::mapToResponseDTO);
    }

    private EnrollmentResponseDTO mapToResponseDTO(Enrollment enrollment) {

        EnrollmentResponseDTO dto = new EnrollmentResponseDTO();

        dto.setId(enrollment.getId());

        dto.setStudentId(enrollment.getStudent().getId());
        dto.setStudentName(
                enrollment.getStudent().getFirstName() + " " +
                        enrollment.getStudent().getLastName()
        );

        dto.setCourseId(enrollment.getCourse().getId());
        dto.setCourseName(enrollment.getCourse().getCourseName());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus());
        dto.setGrade(enrollment.getGrade());
        dto.setRemarks(enrollment.getRemarks());
        dto.setCreatedAt(enrollment.getCreatedAt());
        dto.setUpdatedAt(enrollment.getUpdatedAt());

        return dto;
    }

    @Transactional
    @Override
    public void assignGrade(Long enrollmentId, GradeRequestDTO requestDTO) {

        if (enrollmentId <= 0) {
            throw new RuntimeException("Other type of exception");
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Enrollment not found : " + enrollmentId));

        if (!"ACTIVE".equalsIgnoreCase(enrollment.getStatus())) {
            throw new IllegalStateException(
                    "Grade can only be assigned to ACTIVE enrollments");
        }

        enrollment.setGrade(requestDTO.getGrade());
        enrollment.setRemarks(requestDTO.getRemarks());
        enrollment.setUpdatedAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);
    }

    @Transactional
    @Override
    public StudentReportDTO getStudentReport(Long studentId) {

        if (studentId <= 0) {
            throw new RuntimeException("Other type of exception");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student not found : " + studentId));

        List<Enrollment> enrollments =
                enrollmentRepository.findByStudentId(studentId);

        List<StudentCourseGradeDTO> courseGrades = enrollments.stream()
                .map(enrollment -> {
                    StudentCourseGradeDTO dto = new StudentCourseGradeDTO();
                    dto.setCourseName(enrollment.getCourse().getCourseName());
                    dto.setGrade(enrollment.getGrade());
                    dto.setRemarks(enrollment.getRemarks());
                    return dto;
                })
                .toList();

        double average = enrollments.stream()
                .filter(enrollment -> enrollment.getGrade() != null)
                .mapToDouble(Enrollment::getGrade)
                .average()
                .orElse(0.0);

        StudentReportDTO report = new StudentReportDTO();
        report.setStudentId(student.getId());
        report.setStudentName(
                student.getFirstName() + " " + student.getLastName());
        report.setCourses(courseGrades);
        report.setAverageGrade(average);

        return report;
    }

    @Transactional
    @Override
    public CourseReportDTO getCourseReport(Long courseId) {

        if (courseId <= 0) {
            throw new RuntimeException("Other type of exception");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course not found : " + courseId));

        List<Enrollment> enrollments =
                enrollmentRepository.findByCourseId(courseId);

        List<CourseStudentGradeDTO> students = enrollments.stream()
                .map(enrollment -> {
                    CourseStudentGradeDTO dto = new CourseStudentGradeDTO();

                    dto.setStudentName(
                            enrollment.getStudent().getFirstName() + " " +
                                    enrollment.getStudent().getLastName());

                    dto.setGrade(enrollment.getGrade());
                    dto.setRemarks(enrollment.getRemarks());

                    return dto;
                })
                .toList();

        double average = enrollments.stream()
                .filter(enrollment -> enrollment.getGrade() != null)
                .mapToDouble(Enrollment::getGrade)
                .average()
                .orElse(0.0);

        CourseReportDTO report = new CourseReportDTO();

        report.setCourseId(course.getId());
        report.setCourseName(course.getCourseName());
        report.setTotalStudents(enrollments.size());
        report.setAverageGrade(average);
        report.setStudents(students);

        return report;
    }

}
