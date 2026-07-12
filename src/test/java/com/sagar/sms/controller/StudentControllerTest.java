package com.sagar.sms.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.StudentNotFoundException;
import com.sagar.sms.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    void createStudent_ShouldReturnCreated_WhenRequestIsValid() throws Exception {

        // Arrange
        StudentRequestDTO requestDTO = new StudentRequestDTO();

        requestDTO.setFirstName("Sagar");
        requestDTO.setLastName("Kirtakar");
        requestDTO.setEmail("sagar@gmail.com");
        requestDTO.setPhone("9876543210");
        requestDTO.setDateOfBirth(LocalDate.of(2002, 5, 20));

        // Act & Assert
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Verify
        verify(studentService, times(1))
                .createStd(any(StudentRequestDTO.class));
    }

    @Test
    void createStudent_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        // Arrange
        StudentRequestDTO requestDTO = new StudentRequestDTO();

        requestDTO.setFirstName(null);
        requestDTO.setLastName("Kirtakar");
        requestDTO.setEmail("sa");
        requestDTO.setPhone("9876543210");
        requestDTO.setDateOfBirth(LocalDate.of(2002, 5, 20));

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).createStd(any(StudentRequestDTO.class));

    }

    @Test
    void getAllStudents_ShouldReturnStudentList() throws Exception {

        StudentResponseDTO dto1 = new StudentResponseDTO();
        dto1.setId(1L);
        dto1.setFirstName("Sagar");

        StudentResponseDTO dto2 = new StudentResponseDTO();
        dto2.setId(2L);
        dto2.setFirstName("Rahul");

        when(studentService.getAllStd())
                .thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Sagar"))
                .andExpect(jsonPath("$[1].firstName").value("Rahul"));

        verify(studentService, times(1)).getAllStd();
    }

    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenStudentsNotExist() throws Exception {

        when(studentService.getAllStd()).thenReturn(List.of());

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(jsonPath("$").isEmpty());

        verify(studentService, times(1)).getAllStd();

    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenStudentExists() throws Exception {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Sagar");
        dto.setLastName("Kirtakar");
        dto.setEmail("sagar@gmail.com");

        when(studentService.getStdById(1L)).thenReturn(dto);

        Long id = 1L;

        mockMvc.perform(get("/students/{id}",id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Sagar"))
                .andExpect(jsonPath("$.lastName").value("Kirtakar"));

        verify(studentService, times(1)).getStdById(1L);
    }

    @Test
    void getStudentById_ShouldReturnNotFound_WhenStudentNotExists() throws Exception {

        Long id = 1L;

        when(studentService.getStdById(id))
                .thenThrow(
                        new StudentNotFoundException("Student not found : 1")
                );

        mockMvc.perform(get("/students/{id}",1))
                .andExpect(status()
                        .isNotFound())
                .andExpect(jsonPath("$.msg")
                        .value("Student not found : 1"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Id not available"))
                .andExpect(jsonPath("$.status")
                        .value("NOT_FOUND"))
                .andExpect(jsonPath("$.localDateTime")
                .exists());

        verify(studentService, times(1)).getStdById(id);

    }

    @Test
    void updateStudent_ShouldReturnCreated_WhenRequestIsValid() throws Exception {

        Long id =1L;

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFirstName("Sagar");
        dto.setLastName("Kirtakar");
        dto.setEmail("sagar@gmail.com");
        dto.setDateOfBirth(LocalDate.of(2002, 4, 10));

        mockMvc.perform(put("/students/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(studentService, times(1)).updateStdById(eq(id), any(StudentRequestDTO.class));

    }

    @Test
    void updateStudent_ShouldReturnNotFound_WhenStudentNotExists() throws Exception {

        // Arrange
        Long id = 1L;

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setFirstName("Sagar");
        dto.setLastName("Kirtakar");
        dto.setEmail("sagar@gmail.com");
        dto.setPhone("9876543210");
        dto.setDateOfBirth(LocalDate.of(2002, 4, 10));

        doThrow(new StudentNotFoundException("Student not found : 1"))
                .when(studentService)
                .updateStdById(eq(id), any(StudentRequestDTO.class));

        // Act & Assert
        mockMvc.perform(put("/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg")
                        .value("Student not found : 1"))
                .andExpect(jsonPath("$.status")
                        .value("NOT_FOUND"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Id not available"))
                .andExpect(jsonPath("$.localDateTime").exists());

        // Verify
        verify(studentService, times(1))
                .updateStdById(eq(id), any(StudentRequestDTO.class));
    }

    @Test
    void deleteStudent_ShouldReturnOk_WhenStudentExists() throws Exception {

        Long id = 1L;

        mockMvc.perform(delete("/students/{id}",id))
                .andExpect(status().isOk())
                .andExpect(content().string("Student deleted successfully"));

        verify(studentService, times(1)).deleteStdById(eq(id));
    }

    @Test
    void deleteStudent_ShouldReturnNotFound_WhenStudentNotExists() throws Exception {

        Long id = 1L;

        doThrow(new StudentNotFoundException("Student not found : 1"))
                .when(studentService).deleteStdById(eq(id));

        mockMvc.perform(delete("/students/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.msg")
                        .value("Student not found : 1"))
                .andExpect(jsonPath("$.status")
                        .value("NOT_FOUND"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Id not available"))
                .andExpect(jsonPath("$.localDateTime").exists());

        verify(studentService, times(1)).deleteStdById(eq(id));
    }

    @Test
    void getStudents_ShouldReturnPaginatedStudents() throws Exception {

        // Arrange
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Sagar");

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("firstName").ascending()
        );

        Page<StudentResponseDTO> dtoPage = new PageImpl<>(
                List.of(dto),
                pageable,
                1
        );

        when(studentService.getStudents(pageable))
                .thenReturn(dtoPage);

        // Act & Assert
        mockMvc.perform(get("/students/page")
                        .param("pageNo", "1")
                        .param("pageSize", "5")
                        .param("sortBy", "firstName")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("Sagar"))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(1));

        // Verify
        verify(studentService, times(1))
                .getStudents(pageable);
    }

    @Test
    void getStudents_ShouldUseDefaultPaginationAndSortingValues() throws Exception {

        // Arrange
        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("id").ascending()
        );

        when(studentService.getStudents(pageable))
                .thenReturn(Page.empty(pageable));

        // Act & Assert
        mockMvc.perform(get("/students/page"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.content").isEmpty());

        // Verify
        verify(studentService, times(1))
                .getStudents(pageable);
    }

    @Test
    void getStudents_ShouldReturnBadRequest_WhenPageNoIsInvalid() throws Exception {

        mockMvc.perform(get("/students/page")
                .param("pageNo", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg")
                        .value("Page number must be greater than or equal 1"))
                .andExpect(jsonPath("$.status")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Other exception"));

        verify(studentService, never())
                .getStudents(any(Pageable.class));

    }

    @Test
    void getStudents_ShouldReturnBadRequest_WhenPageSizeIsInvalid() throws Exception {

        // Act & Assert
        mockMvc.perform(get("/students/page")
                        .param("pageSize", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg")
                        .value("Page size must be greater than or equal 1"))
                .andExpect(jsonPath("$.status")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Other exception"));

        // Verify
        verify(studentService, never())
                .getStudents(any(Pageable.class));
    }

    @Test
    void searchStudents_ShouldReturnMatchingStudents_WhenSearchIsProvided() throws Exception {

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Sagar");

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("id").ascending()
        );

        Page<StudentResponseDTO> dtoPage = new PageImpl<>(
                List.of(dto),
                pageable,
                1
        );

        when(studentService.searchStudents("Sagar", pageable))
                .thenReturn(dtoPage);

        mockMvc.perform(get("/students/search")
                .param("firstName", "Sagar")
                .param("pageNo", "1")
                .param("pageSize", "5")
                .param("sortBy", "id")
                .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("Sagar"))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5));

        verify(studentService, times(1)).searchStudents("Sagar", pageable);
    }

    @Test
    void searchStudents_ShouldReturnBadRequest_WhenFirstNameIsMissing() throws Exception {

        mockMvc.perform(get("/students/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]")
                        .value("Request param is missing"))
                .andExpect(jsonPath("$.status")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.localDateTime")
                        .exists())
                .andExpect(jsonPath("$.msg").exists());

        verify(studentService, never()).searchStudents(anyString(), any(Pageable.class));
    }

    @Test
    void searchStudents_ShouldReturnEmptyPage_WhenNoMatchFound() throws Exception {

        String search = "Unknown";

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("id").ascending()
        );

        Page<StudentResponseDTO> emptyPage =
                Page.empty(pageable);

        when(studentService.searchStudents(search, pageable))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/students/search")
                        .param("firstName", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5));

        verify(studentService, times(1)).searchStudents(search, pageable);
    }

}
