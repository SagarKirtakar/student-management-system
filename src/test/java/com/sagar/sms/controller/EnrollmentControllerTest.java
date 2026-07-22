package com.sagar.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagar.sms.dto.*;
import com.sagar.sms.exception.CourseNotFoundException;
import com.sagar.sms.services.CourseService;
import com.sagar.sms.services.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @Test
    void createEnrollment_ShouldReturn201Created() throws Exception {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(1L);
        requestDTO.setEnrollmentDate(LocalDate.now());
        requestDTO.setStatus("ACTIVE");

        doNothing().when(enrollmentService)
                .createEnrollment(any(EnrollmentRequestDTO.class));

        // Act & Assert
        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Verify
        verify(enrollmentService, times(1))
                .createEnrollment(any(EnrollmentRequestDTO.class));
    }

    @Test
    void getAllEnrollments_ShouldReturnEnrollmentList() throws Exception {

        // Arrange
        EnrollmentResponseDTO responseDTO = new EnrollmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStudentId(1L);
        responseDTO.setStudentName("Sagar Kirtakar");
        responseDTO.setCourseId(1L);
        responseDTO.setCourseName("Java");
        responseDTO.setEnrollmentDate(LocalDate.now());
        responseDTO.setStatus("ACTIVE");
        responseDTO.setCreatedAt(LocalDateTime.now());
        responseDTO.setUpdatedAt(LocalDateTime.now());

        when(enrollmentService.getAllEnrollments())
                .thenReturn(List.of(responseDTO));

        // Act & Assert
        mockMvc.perform(get("/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].studentId").value(1))
                .andExpect(jsonPath("$[0].studentName").value("Sagar Kirtakar"))
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].courseName").value("Java"))
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        // Verify
        verify(enrollmentService, times(1))
                .getAllEnrollments();
    }

    @Test
    void getEnrollmentById_ShouldReturnEnrollment() throws Exception {

        // Arrange
        EnrollmentResponseDTO responseDTO = new EnrollmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStudentId(1L);
        responseDTO.setStudentName("Sagar Kirtakar");
        responseDTO.setCourseId(1L);
        responseDTO.setCourseName("Java");
        responseDTO.setEnrollmentDate(LocalDate.now());
        responseDTO.setStatus("ACTIVE");
        responseDTO.setCreatedAt(LocalDateTime.now());
        responseDTO.setUpdatedAt(LocalDateTime.now());

        when(enrollmentService.getEnrollmentById(1L))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/enrollments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.studentName").value("Sagar Kirtakar"))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.courseName").value("Java"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        // Verify
        verify(enrollmentService, times(1))
                .getEnrollmentById(1L);
    }

    @Test
    void updateEnrollment_ShouldReturn204NoContent() throws Exception {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();
        requestDTO.setStudentId(1L);
        requestDTO.setCourseId(2L);
        requestDTO.setEnrollmentDate(LocalDate.now());
        requestDTO.setStatus("COMPLETED");

        doNothing().when(enrollmentService)
                .updateEnrollmentById(eq(1L), any(EnrollmentRequestDTO.class));

        // Act & Assert
        mockMvc.perform(put("/enrollments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());

        // Verify
        verify(enrollmentService, times(1))
                .updateEnrollmentById(eq(1L), any(EnrollmentRequestDTO.class));
    }

    @Test
    void deleteEnrollment_ShouldReturn200Ok() throws Exception {

        // Arrange
        doNothing().when(enrollmentService)
                .deleteEnrollmentById(1L);

        // Act & Assert
        mockMvc.perform(delete("/enrollments/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrollment deleted successfully"));

        // Verify
        verify(enrollmentService, times(1))
                .deleteEnrollmentById(1L);
    }

    @Test
    void getEnrollments_ShouldReturnPaginatedEnrollments() throws Exception {

        // Arrange
        EnrollmentResponseDTO responseDTO = new EnrollmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStudentId(1L);
        responseDTO.setStudentName("Sagar Kirtakar");
        responseDTO.setCourseId(1L);
        responseDTO.setCourseName("Java");
        responseDTO.setEnrollmentDate(LocalDate.now());
        responseDTO.setStatus("ACTIVE");

        Pageable pageable = PageRequest.of(0, 5);

        Page<EnrollmentResponseDTO> page =
                new PageImpl<>(List.of(responseDTO), pageable, 1);

        when(enrollmentService.getEnrollments(any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/enrollments/page")
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "id")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].studentName")
                        .value("Sagar Kirtakar"))
                .andExpect(jsonPath("$.content[0].courseName")
                        .value("Java"))
                .andExpect(jsonPath("$.content[0].status")
                        .value("ACTIVE"));

        // Verify
        verify(enrollmentService, times(1))
                .getEnrollments(any(Pageable.class));
    }

    @Test
    void searchEnrollments_ShouldReturnMatchingEnrollments() throws Exception {

        // Arrange
        EnrollmentResponseDTO responseDTO = new EnrollmentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStudentId(1L);
        responseDTO.setStudentName("Sagar Kirtakar");
        responseDTO.setCourseId(1L);
        responseDTO.setCourseName("Java");
        responseDTO.setEnrollmentDate(LocalDate.now());
        responseDTO.setStatus("ACTIVE");

        Pageable pageable = PageRequest.of(0, 5);

        Page<EnrollmentResponseDTO> page =
                new PageImpl<>(List.of(responseDTO), pageable, 1);

        when(enrollmentService.searchEnrollments(
                eq("ACTIVE"),
                any(Pageable.class)))
                .thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/enrollments/search")
                        .param("status", "ACTIVE")
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "id")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].studentName")
                        .value("Sagar Kirtakar"))
                .andExpect(jsonPath("$.content[0].courseName")
                        .value("Java"))
                .andExpect(jsonPath("$.content[0].status")
                        .value("ACTIVE"));

        // Verify
        verify(enrollmentService, times(1))
                .searchEnrollments(eq("ACTIVE"), any(Pageable.class));
    }

    @Test
    void createEnrollment_ShouldReturn400_WhenValidationFails() throws Exception {

        // Arrange
        EnrollmentRequestDTO requestDTO = new EnrollmentRequestDTO();

        // Act & Assert
        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        // Verify
        verify(enrollmentService, never())
                .createEnrollment(any());
    }

    @Test
    void assignGrade_ShouldReturnNoContent_WhenRequestIsValid() throws Exception {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(95.0);
        requestDTO.setRemarks("Excellent");

        doNothing().when(enrollmentService)
                .assignGrade(eq(1L), any(GradeRequestDTO.class));

        // Act & Assert
        mockMvc.perform(put("/enrollments/{id}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNoContent());

        verify(enrollmentService, times(1))
                .assignGrade(eq(1L), any(GradeRequestDTO.class));
    }

    @Test
    void assignGrade_ShouldReturn400_WhenGradeIsLessThanZero() throws Exception {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(-10.0);
        requestDTO.setRemarks("Invalid Grade");

        // Act & Assert
        mockMvc.perform(put("/enrollments/{id}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(enrollmentService, never())
                .assignGrade(anyLong(), any(GradeRequestDTO.class));
    }

    @Test
    void assignGrade_ShouldReturn400_WhenGradeIsGreaterThan100() throws Exception {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(110.0);
        requestDTO.setRemarks("Invalid Grade");

        // Act & Assert
        mockMvc.perform(put("/enrollments/{id}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(enrollmentService, never())
                .assignGrade(anyLong(), any(GradeRequestDTO.class));
    }

    @Test
    void assignGrade_ShouldReturn400_WhenGradeIsNull() throws Exception {

        // Arrange
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setGrade(null);
        requestDTO.setRemarks("Grade is required");

        // Act & Assert
        mockMvc.perform(put("/enrollments/{id}/grade", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(enrollmentService, never())
                .assignGrade(anyLong(), any(GradeRequestDTO.class));
    }


}
