package com.sagar.sms.service;

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
import com.sagar.sms.services.EnrollmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void createEnrollment_ShouldSaveEnrollment_WhenStudentAndCourseExist() {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(1L);
        requestDTO.setEnrollmentDate(LocalDate.now());
        requestDTO.setStatus("ACTIVE");

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        // Act
        enrollmentService.createEnrollment(requestDTO);

        // Assert
        verify(studentRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1))
                .save(any(Enrollment.class));
    }

    @Test
    void createEnrollment_ShouldThrowStudentNotFoundException_WhenStudentDoesNotExist() {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentNotFoundException exception =
                assertThrows(StudentNotFoundException.class,
                        () -> enrollmentService.createEnrollment(requestDTO));

        assertEquals("Student not found : 1", exception.getMessage());

        verify(studentRepository).findById(1L);
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void createEnrollment_ShouldThrowCourseNotFoundException_WhenCourseDoesNotExist() {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(1L);

        Student student = new Student();
        student.setId(1L);

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        CourseNotFoundException exception =
                assertThrows(CourseNotFoundException.class,
                        () -> enrollmentService.createEnrollment(requestDTO));

        assertEquals("Course not found : 1", exception.getMessage());

        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void getAllEnrollments_ShouldReturnEnrollmentList_WhenEnrollmentsExist() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        when(enrollmentRepository.findAll())
                .thenReturn(List.of(enrollment));

        // Act
        List<EnrollmentResponseDTO> result =
                enrollmentService.getAllEnrollments();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Sagar Kirtakar", result.get(0).getStudentName());
        assertEquals("Java", result.get(0).getCourseName());
        assertEquals("ACTIVE", result.get(0).getStatus());

        // Verify
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void getAllEnrollments_ShouldReturnEmptyList_WhenNoEnrollmentsExist() {

        // Arrange
        when(enrollmentRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        List<EnrollmentResponseDTO> result =
                enrollmentService.getAllEnrollments();

        // Assert
        assertTrue(result.isEmpty());

        // Verify
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void getEnrollmentById_ShouldReturnEnrollment_WhenEnrollmentExists() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        // Act
        EnrollmentResponseDTO result =
                enrollmentService.getEnrollmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getStudentId());
        assertEquals("Sagar Kirtakar", result.getStudentName());
        assertEquals(1L, result.getCourseId());
        assertEquals("Java", result.getCourseName());
        assertEquals("ACTIVE", result.getStatus());

        // Verify
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void getEnrollmentById_ShouldThrowEnrollmentNotFoundException_WhenEnrollmentDoesNotExist() {

        // Arrange
        long enrollmentId = 1L;

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        EnrollmentNotFoundException exception =
                assertThrows(
                        EnrollmentNotFoundException.class,
                        () -> enrollmentService.getEnrollmentById(enrollmentId)
                );

        assertEquals(
                "Enrollment not found : 1",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository, times(1))
                .findById(enrollmentId);
    }

    @Test
    void getEnrollmentById_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        // Arrange
        long invalidId = -1L;

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService.getEnrollmentById(invalidId)
                );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository, never())
                .findById(anyLong());
    }

    @Test
    void updateEnrollment_ShouldUpdateEnrollment_WhenRequestIsValid() {

        // Arrange
        long enrollmentId = 1L;

        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(2L);
        requestDTO.setCourseId(2L);
        requestDTO.setEnrollmentDate(LocalDate.now());
        requestDTO.setStatus("COMPLETED");

        Student student = new Student();
        student.setId(2L);
        student.setFirstName("Rahul");
        student.setLastName("Sharma");

        Course course = new Course();
        course.setId(2L);
        course.setCourseName("Spring Boot");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.of(enrollment));

        when(studentRepository.findById(2L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(2L))
                .thenReturn(Optional.of(course));

        // Act
        enrollmentService.updateEnrollmentById(enrollmentId, requestDTO);

        // Assert
        assertEquals(student, enrollment.getStudent());
        assertEquals(course, enrollment.getCourse());
        assertEquals("COMPLETED", enrollment.getStatus());
        assertNotNull(enrollment.getUpdatedAt());

        // Verify
        verify(enrollmentRepository).findById(enrollmentId);
        verify(studentRepository).findById(2L);
        verify(courseRepository).findById(2L);
        verify(enrollmentRepository).save(enrollment);
    }

    @Test
    void updateEnrollment_ShouldThrowEnrollmentNotFoundException_WhenEnrollmentDoesNotExist() {

        // Arrange
        long enrollmentId = 1L;

        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        EnrollmentNotFoundException exception =
                assertThrows(
                        EnrollmentNotFoundException.class,
                        () -> enrollmentService.updateEnrollmentById(enrollmentId, requestDTO)
                );

        assertEquals(
                "Enrollment not found : 1",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository).findById(enrollmentId);
        verify(studentRepository, never()).findById(anyLong());
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void updateEnrollment_ShouldThrowStudentNotFoundException_WhenStudentDoesNotExist() {

        // Arrange
        long enrollmentId = 1L;

        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);

        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(2L);
        requestDTO.setCourseId(1L);

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.of(enrollment));

        when(studentRepository.findById(2L))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentNotFoundException exception =
                assertThrows(
                        StudentNotFoundException.class,
                        () -> enrollmentService.updateEnrollmentById(enrollmentId, requestDTO)
                );

        assertEquals(
                "Student not found : 2",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository).findById(enrollmentId);
        verify(studentRepository).findById(2L);
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void updateEnrollment_ShouldThrowCourseNotFoundException_WhenCourseDoesNotExist() {

        // Arrange
        long enrollmentId = 1L;

        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);

        Student student = new Student();
        student.setId(2L);

        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(2L);
        requestDTO.setCourseId(5L);

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.of(enrollment));

        when(studentRepository.findById(2L))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(5L))
                .thenReturn(Optional.empty());

        // Act & Assert
        CourseNotFoundException exception =
                assertThrows(
                        CourseNotFoundException.class,
                        () -> enrollmentService.updateEnrollmentById(enrollmentId, requestDTO)
                );

        assertEquals(
                "Course not found : 5",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository).findById(enrollmentId);
        verify(studentRepository).findById(2L);
        verify(courseRepository).findById(5L);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void updateEnrollment_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService.updateEnrollmentById(-1L, requestDTO)
                );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository, never()).findById(anyLong());
        verify(studentRepository, never()).findById(anyLong());
        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void deleteEnrollment_ShouldDeleteEnrollment_WhenEnrollmentExists() {

        // Arrange
        long enrollmentId = 1L;

        Enrollment enrollment = new Enrollment();
        enrollment.setId(enrollmentId);

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.of(enrollment));

        // Act
        enrollmentService.deleteEnrollmentById(enrollmentId);

        // Verify
        verify(enrollmentRepository, times(1))
                .findById(enrollmentId);

        verify(enrollmentRepository, times(1))
                .deleteById(enrollmentId);
    }

    @Test
    void deleteEnrollment_ShouldThrowEnrollmentNotFoundException_WhenEnrollmentDoesNotExist() {

        // Arrange
        long enrollmentId = 1L;

        when(enrollmentRepository.findById(enrollmentId))
                .thenReturn(Optional.empty());

        // Act & Assert
        EnrollmentNotFoundException exception =
                assertThrows(
                        EnrollmentNotFoundException.class,
                        () -> enrollmentService.deleteEnrollmentById(enrollmentId)
                );

        assertEquals(
                "Enrollment not found : 1",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository, times(1))
                .findById(enrollmentId);

        verify(enrollmentRepository, never())
                .deleteById(anyLong());
    }

    @Test
    void deleteEnrollment_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        // Arrange
        long invalidId = -1L;

        // Act & Assert
        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> enrollmentService.deleteEnrollmentById(invalidId)
                );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        // Verify
        verify(enrollmentRepository, never())
                .findById(anyLong());

        verify(enrollmentRepository, never())
                .deleteById(anyLong());
    }

    @Test
    void getEnrollments_ShouldReturnPaginatedEnrollments() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 5);

        Page<Enrollment> page = new PageImpl<>(
                List.of(enrollment),
                pageable,
                1
        );

        when(enrollmentRepository.findAll(pageable))
                .thenReturn(page);

        // Act
        Page<EnrollmentResponseDTO> result =
                enrollmentService.getEnrollments(pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("Sagar Kirtakar",
                result.getContent().get(0).getStudentName());
        assertEquals("Java",
                result.getContent().get(0).getCourseName());

        // Verify
        verify(enrollmentRepository).findAll(pageable);
    }

    @Test
    void searchEnrollments_ShouldReturnMatchingEnrollments_WhenStatusExists() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 5);

        Page<Enrollment> page =
                new PageImpl<>(List.of(enrollment));

        when(enrollmentRepository.findByStatusContainingIgnoreCase(
                "ACTIVE",
                pageable))
                .thenReturn(page);

        // Act
        Page<EnrollmentResponseDTO> result =
                enrollmentService.searchEnrollments(
                        "ACTIVE",
                        pageable);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals("ACTIVE",
                result.getContent().get(0).getStatus());

        // Verify
        verify(enrollmentRepository)
                .findByStatusContainingIgnoreCase(
                        "ACTIVE",
                        pageable);
    }

    @Test
    void searchEnrollments_ShouldReturnAllEnrollments_WhenStatusIsBlank() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 5);

        Page<Enrollment> page =
                new PageImpl<>(Collections.emptyList());

        when(enrollmentRepository.findAll(pageable))
                .thenReturn(page);

        // Act
        Page<EnrollmentResponseDTO> result =
                enrollmentService.searchEnrollments(
                        "",
                        pageable);

        // Assert
        assertTrue(result.isEmpty());

        // Verify
        verify(enrollmentRepository)
                .findAll(pageable);

        verify(enrollmentRepository, never())
                .findByStatusContainingIgnoreCase(anyString(), any());
    }

    @Test
    void searchEnrollments_ShouldReturnEmptyPage_WhenStatusNotFound() {

        // Arrange
        Pageable pageable = PageRequest.of(0, 5);

        Page<Enrollment> page =
                new PageImpl<>(Collections.emptyList());

        when(enrollmentRepository.findByStatusContainingIgnoreCase(
                "COMPLETED",
                pageable))
                .thenReturn(page);

        // Act
        Page<EnrollmentResponseDTO> result =
                enrollmentService.searchEnrollments(
                        "COMPLETED",
                        pageable);

        // Assert
        assertTrue(result.isEmpty());

        // Verify
        verify(enrollmentRepository)
                .findByStatusContainingIgnoreCase(
                        "COMPLETED",
                        pageable);
    }

    @Test
    void assignGrade_ShouldUpdateGrade_WhenEnrollmentExists() {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(92.0);
        requestDTO.setRemarks("Outstanding");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStatus("ACTIVE");

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        // Act
        enrollmentService.assignGrade(1L, requestDTO);

        // Assert
        assertEquals(92.0, enrollment.getGrade());
        assertEquals("Outstanding", enrollment.getRemarks());

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void assignGrade_ShouldThrowException_WhenEnrollmentNotFound() {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(90.0);
        requestDTO.setRemarks("Excellent");

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        EnrollmentNotFoundException exception = assertThrows(
                EnrollmentNotFoundException.class,
                () -> enrollmentService.assignGrade(1L, requestDTO)
        );

        assertEquals("Enrollment not found : 1", exception.getMessage());

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_ShouldThrowException_WhenEnrollmentStatusIsNotActive() {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(85.0);
        requestDTO.setRemarks("Good");

        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStatus("PENDING");

        when(enrollmentRepository.findById(1L))
                .thenReturn(Optional.of(enrollment));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> enrollmentService.assignGrade(1L, requestDTO)
        );

        assertEquals(
                "Grade can only be assigned to ACTIVE enrollments",
                exception.getMessage()
        );

        verify(enrollmentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void assignGrade_ShouldThrowException_WhenInvalidId() {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(90.0);
        requestDTO.setRemarks("Excellent");

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> enrollmentService.assignGrade(0L, requestDTO)
        );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        verify(enrollmentRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    void getStudentReport_ShouldReturnReport_WhenStudentExists() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(90.0);
        enrollment.setRemarks("Excellent");

        when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));

        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(List.of(enrollment));

        // Act
        StudentReportDTO report = enrollmentService.getStudentReport(1L);

        // Assert
        assertNotNull(report);
        assertEquals(1L, report.getStudentId());
        assertEquals("Sagar Kirtakar", report.getStudentName());
        assertEquals(1, report.getCourses().size());
        assertEquals(90.0, report.getAverageGrade());

        verify(studentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).findByStudentId(1L);
    }

    @Test
    void getStudentReport_ShouldThrowException_WhenStudentNotFound() {

        // Arrange
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentNotFoundException exception = assertThrows(
                StudentNotFoundException.class,
                () -> enrollmentService.getStudentReport(1L)
        );

        assertEquals("Student not found : 1", exception.getMessage());

        verify(studentRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).findByStudentId(anyLong());
    }

    @Test
    void getStudentReport_ShouldThrowException_WhenInvalidStudentId() {

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> enrollmentService.getStudentReport(0L)
        );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        verify(studentRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).findByStudentId(anyLong());
    }

    @Test
    void getCourseReport_ShouldReturnReport_WhenCourseExists() {

        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");
        student.setLastName("Kirtakar");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setGrade(95.0);
        enrollment.setRemarks("Excellent");

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(enrollmentRepository.findByCourseId(1L))
                .thenReturn(List.of(enrollment));

        // Act
        CourseReportDTO report = enrollmentService.getCourseReport(1L);

        // Assert
        assertNotNull(report);
        assertEquals(1L, report.getCourseId());
        assertEquals("Java", report.getCourseName());
        assertEquals(1, report.getTotalStudents());
        assertEquals(95.0, report.getAverageGrade());

        assertEquals(1, report.getStudents().size());
        assertEquals(
                "Sagar Kirtakar",
                report.getStudents().get(0).getStudentName());

        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, times(1)).findByCourseId(1L);
    }

    @Test
    void getCourseReport_ShouldThrowException_WhenCourseNotFound() {

        // Arrange
        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> enrollmentService.getCourseReport(1L)
        );

        assertEquals("Course not found : 1", exception.getMessage());

        verify(courseRepository, times(1)).findById(1L);
        verify(enrollmentRepository, never()).findByCourseId(anyLong());
    }

    @Test
    void getCourseReport_ShouldThrowException_WhenInvalidCourseId() {

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> enrollmentService.getCourseReport(0L)
        );

        assertEquals(
                "Other type of exception",
                exception.getMessage()
        );

        verify(courseRepository, never()).findById(anyLong());
        verify(enrollmentRepository, never()).findByCourseId(anyLong());
    }



}