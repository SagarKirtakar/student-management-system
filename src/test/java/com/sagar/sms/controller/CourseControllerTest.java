package com.sagar.sms.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.exception.CourseNotFoundException;
import com.sagar.sms.services.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseService courseService;

    @Test
    void createCourse_ShouldReturnCreated_WhenRequestIsValid() throws Exception {

        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setCourseCode("Java199");
        dto.setCourseName("Java");

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                        .andExpect(status().isCreated());

        verify(courseService, times(1))
                .createCourse(any(CourseRequestDTO.class));
    }

    @Test
    void createCourse_ShouldReturnBadRequest_WhenRequestIsInvalid() throws Exception {
        // Arrange
        CourseRequestDTO invalidRequestDTO = new CourseRequestDTO();
        invalidRequestDTO.setCourseCode("");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(invalidRequestDTO);

        // Act & Assert
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value("Validation is Failed"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.localDateTime").exists());

        verifyNoInteractions(courseService);
    }

    @Test
    void getAllCourses_ShouldReturnCourseList() throws Exception {
        // Arrange
        CourseResponseDTO course1 = new CourseResponseDTO();
        course1.setId(1L);
        course1.setCourseCode("DS-128");

        List<CourseResponseDTO> mockList = Collections.singletonList(course1);

        when(courseService.getAllCourse()).thenReturn(mockList);

        // Act & Assert
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].courseCode").value("DS-128"));


        // Verify
        verify(courseService).getAllCourse();
    }

    @Test
    void getAllCourses_ShouldReturnEmptyList_WhenCoursesNotExist() throws Exception {
        // Arrange
        when(courseService.getAllCourse()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andExpect(jsonPath("$").isEmpty());

        // Verify
        verify(courseService).getAllCourse();
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenCourseExists() throws Exception {
        // Arrange
        long courseId = 1L;
        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(courseId);
        responseDTO.setCourseName("Data Structures");
        responseDTO.setCourseCode("DS-128");

        when(courseService.getCourseById(courseId)).thenReturn(responseDTO);

        mockMvc.perform(get("/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseId))
                .andExpect(jsonPath("$.courseCode").value("DS-128"))
                .andExpect(jsonPath("$.courseName").value("Data Structures"));

        // Verify
        verify(courseService).getCourseById(courseId);
    }

    @Test
    void getCourseById_ShouldReturnNotFound_WhenCourseNotExists() throws Exception {

        long missingCourseId = 99L;

        when(courseService.getCourseById(missingCourseId))
                .thenThrow(new CourseNotFoundException("Course not found : " + missingCourseId));

        mockMvc.perform(get("/courses/{id}", missingCourseId))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(courseService).getCourseById(missingCourseId);
    }

    @Test
    void updateCourse_ShouldReturnNoContent_WhenRequestIsValid() throws Exception {
        // Arrange
        long courseId = 1L;
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setCourseCode("DS-200");
        requestDTO.setCourseName("Data Structures");
        requestDTO.setDescription("Valid sample description");

        String jsonPayload = objectMapper.writeValueAsString(requestDTO);

        // Act & Assert
        mockMvc.perform(put("/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isNoContent());

        // Verify
        verify(courseService)
                .updateCourseById(eq(courseId), any(CourseRequestDTO.class));
    }

    @Test
    void updateCourse_ShouldReturnNotFound_WhenCourseNotExists() throws Exception {

        long courseId = 99L;

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setCourseName("Data Structures");
        requestDTO.setCourseCode("DS-200");

        doThrow(new CourseNotFoundException("Course not found : " + courseId))
                .when(courseService)
                .updateCourseById(eq(courseId), any(CourseRequestDTO.class));

        mockMvc.perform(put("/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                        .andExpect(status().isNotFound());

        verify(courseService, times(1))
                .updateCourseById(eq(courseId), any(CourseRequestDTO.class));
    }

    @Test
    void deleteCourse_ShouldReturnOk_WhenCourseExists() throws Exception {
        // Arrange
        long courseId = 1L;

        mockMvc.perform(delete("/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().string("Course deleted successfully"));

        verify(courseService).deleteCourseById(courseId);
    }

    @Test
    void deleteCourse_ShouldReturnNotFound_WhenCourseNotExists() throws Exception {

        long courseId = 99L;

        doThrow(new CourseNotFoundException("Course not found : 99"))
                .when(courseService)
                .deleteCourseById(courseId);

        mockMvc.perform(delete("/courses/{id}", courseId))
                .andExpect(status().isNotFound());

        verify(courseService, times(1))
                .deleteCourseById(courseId);
    }

    @Test
    void getCourses_ShouldReturnPaginatedCourses() throws Exception {

        // Arrange
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(1L);
        dto.setCourseCode("DS-128");
        dto.setCourseName("Data Structures");

        Pageable pageable = PageRequest.of(
                1,
                10,
                Sort.by("courseCode").ascending()
        );

        Page<CourseResponseDTO> dtoPage = new PageImpl<>(
                List.of(dto),
                pageable,
                1
        );

        when(courseService.getCourses(any(Pageable.class)))
                .thenReturn(dtoPage);

        // Act & Assert
        mockMvc.perform(get("/courses/page")
                        .param("pageNo", "2")
                        .param("pageSize", "10")
                        .param("sortBy", "courseCode")
                        .param("sortDir", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].courseCode").value("DS-128"))
                .andExpect(jsonPath("$.content[0].courseName").value("Data Structures"));

        verify(courseService, times(1))
                .getCourses(any(Pageable.class));
    }

    @Test
    void getCourses_ShouldUseDefaultPaginationAndSortingValues() throws Exception {
        // Arrange
        Pageable defaultPageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        Page<CourseResponseDTO> emptyPage = Page.empty(defaultPageable);

        when(courseService.getCourses(any(Pageable.class)))
                .thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/courses/page")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5));

        // Verify
        verify(courseService).getCourses(defaultPageable);
    }

    @Test
    void getCourses_ShouldReturnBadRequest_WhenPageNoIsInvalid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/courses/page")
                        .param("pageNo", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCourses_ShouldReturnBadRequest_WhenPageSizeIsInvalid() throws Exception {

        mockMvc.perform(get("/courses/page")
                        .param("pageSize", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg")
                        .value("Page size must be greater than or equal 1"))
                .andExpect(jsonPath("$.status")
                        .value("BAD_REQUEST"))
                .andExpect(jsonPath("$.details[0]")
                        .value("Other exception"))
                .andExpect(jsonPath("$.localDateTime")
                        .exists());

        verify(courseService, never())
                .getCourses(any(Pageable.class));
    }

    @Test
    void searchCourses_ShouldReturnMatchingCourses_WhenSearchIsProvided() throws Exception {
        // Arrange
        String searchKeyword = "Java";
        Pageable expectedPageable = PageRequest.of(0, 5, Sort.by("id").ascending());

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCourseName("Advanced Java Programming");
        responseDTO.setCourseCode("JV-101");

        List<CourseResponseDTO> dtoList = Collections.singletonList(responseDTO);
        Page<CourseResponseDTO> mockPage = new PageImpl<>(dtoList, expectedPageable, dtoList.size());

        when(courseService.searchCourses(eq(searchKeyword), eq(expectedPageable))).thenReturn(mockPage);

        // Act & Assert
        mockMvc.perform(get("/courses/search")
                        .param("courseName", searchKeyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].courseName").value("Advanced Java Programming"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].courseCode").value("JV-101"))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5));

        // Verify
        verify(courseService).searchCourses(eq(searchKeyword), eq(expectedPageable));
    }

    @Test
    void searchCourses_ShouldReturnBadRequest_WhenCourseNameIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/courses/search")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.details[0]")
                        .value("Request param is missing"))
                        .andExpect(jsonPath("$.localDateTime").exists());
    }

    @Test
    void searchCourses_ShouldReturnEmptyPage_WhenNoMatchFound() throws Exception {
        // Arrange
        String searchKeyword = "UnknownCourse";
        Pageable expectedPageable = PageRequest.of(0, 5, Sort.by("id").ascending());
        Page<CourseResponseDTO> emptyPage = Page.empty(expectedPageable);

        when(courseService.searchCourses(eq(searchKeyword), eq(expectedPageable))).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/courses/search")
                        .param("courseName", searchKeyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(5));

        // Verify
        verify(courseService, times(1)).searchCourses(eq(searchKeyword), eq(expectedPageable));
    }

















}
