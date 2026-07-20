package com.sagar.sms.services;

import com.sagar.sms.dto.EnrollmentRequestDTO;
import com.sagar.sms.dto.EnrollmentResponseDTO;
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
        dto.setCreatedAt(enrollment.getCreatedAt());
        dto.setUpdatedAt(enrollment.getUpdatedAt());

        return dto;
    }
}
