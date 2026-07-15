package com.sagar.sms.service;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.entity.Course;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.CourseCodeAlreadyExistsException;
import com.sagar.sms.exception.CourseNotFoundException;
import com.sagar.sms.repository.CourseRepository;
import com.sagar.sms.services.CourseService;
import com.sagar.sms.services.CourseServiceImpl;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void createCourse_ShouldSaveCourse_WhenCourseCodeDoesNotExist() {

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setCourseName("Data Science");
        requestDTO.setCourseCode("DS-128");
        requestDTO.setDescription("Data science is the best course");

        Course course = new Course();
        course.setCourseName("Data Science");
        course.setCourseCode("DS-128");
        course.setDescription("Data science is the best course");
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setCourseName("Data Science");
        savedCourse.setCourseCode("DS-128");
        savedCourse.setDescription("Data science is the best course");

        when(courseRepository.existsByCourseCode("DS-128"))
                .thenReturn(false);

        when(modelMapper.map(requestDTO, Course.class))
                .thenReturn(course);

        when(courseRepository.save(course)).thenReturn(savedCourse);

        courseService.createCourse(requestDTO);

        Assertions.assertNotNull(course.getCreatedAt());
        Assertions.assertNotNull(course.getUpdatedAt());
        verify(courseRepository).existsByCourseCode("DS-128");
        verify(modelMapper).map(requestDTO, Course.class);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void createCourse_ShouldThrowException_WhenCourseCodeAlreadyExists() {

        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setCourseCode("DS-128");

        when(courseRepository.existsByCourseCode(requestDTO.getCourseCode())).thenReturn(true);

        CourseCodeAlreadyExistsException exception = Assertions.assertThrows(
                CourseCodeAlreadyExistsException.class,
                () -> courseService.createCourse(requestDTO)
        );

        Assertions.assertEquals("Course code already exists", exception.getMessage());

        verify(courseRepository).existsByCourseCode(requestDTO.getCourseCode());
        verify(modelMapper, never()).map(any(), any());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void getAllCourse_ShouldReturnCourseList_WhenCoursesExist() {

        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseName("Java");
        course1.setCourseCode("Java125");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseName("Python");
        course2.setCourseCode("Python144");

        CourseResponseDTO dto1 = new CourseResponseDTO();
        dto1.setId(1L);
        dto1.setCourseName("Java");
        dto1.setCourseCode("Java125");

        CourseResponseDTO dto2 = new CourseResponseDTO();
        dto2.setId(2L);
        dto2.setCourseName("Python");
        dto2.setCourseCode("Python144");

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        when(modelMapper.map(course1, CourseResponseDTO.class)).thenReturn(dto1);

        when(modelMapper.map(course2, CourseResponseDTO.class)).thenReturn(dto2);

        List<CourseResponseDTO> responseDTOList = courseService.getAllCourse();

        Assertions.assertNotNull(responseDTOList);
        Assertions.assertEquals("Java", responseDTOList.get(0).getCourseName());
        Assertions.assertEquals("Python", responseDTOList.get(1).getCourseName());
        Assertions.assertEquals(2, responseDTOList.size());

        verify(courseRepository, times(1)).findAll();
        verify(modelMapper, times(2)).map(any(Course.class), eq(CourseResponseDTO.class));

    }

    @Test
    void getAllCourse_ShouldReturnEmptyList_WhenCoursesNotExist() {

        when(courseRepository.findAll()).thenReturn(List.of());

        List<CourseResponseDTO> courseList = courseService.getAllCourse();

        Assertions.assertNotNull(courseList);
        Assertions.assertTrue(courseList.isEmpty());
        Assertions.assertEquals(0, courseList.size());

        verify(courseRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());

    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenCourseExists() throws CourseNotFoundException {
        // Arrange
        long courseId = 1L;

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Java");

        CourseResponseDTO expectedResponse = new CourseResponseDTO();
        expectedResponse.setId(1L);
        expectedResponse.setCourseName("Java");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(modelMapper.map(course, CourseResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        CourseResponseDTO actualResponse = courseService.getCourseById(courseId);

        // Assert
        Assertions.assertNotNull(actualResponse);
        Assertions.assertEquals(expectedResponse, actualResponse);
        Assertions.assertEquals("Java", actualResponse.getCourseName());

        // Verify
        verify(courseRepository).findById(courseId);
        verify(modelMapper).map(course, CourseResponseDTO.class);
    }

    @Test
    void getCourseById_ShouldThrowCourseNotFoundException_WhenCourseNotExists() {

        long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        CourseNotFoundException ex = Assertions.assertThrows(
                CourseNotFoundException.class,
                () -> courseService.getCourseById(courseId));

        Assertions.assertEquals("Course not found : "+courseId, ex.getMessage());

        verify(courseRepository, times(1)).findById(courseId);
        verify(modelMapper, never()).map(any(), any());

    }

    @Test
    void getCourseById_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        long courseId = 0L;

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> courseService.getCourseById(courseId));

        Assertions.assertEquals("Other type of exception", exception.getMessage());

        verifyNoInteractions(courseRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void updateCourseById_ShouldUpdateCourse_WhenCourseExists() {

        long courseId = 1L;
        CourseRequestDTO dto = new CourseRequestDTO();
        dto.setCourseName("Java");
        dto.setCourseCode("Java177");

        Course validCourse = new Course();
        validCourse.setId(1L);
        validCourse.setCourseName("Java");
        validCourse.setCourseCode("Java165");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(validCourse));

        courseService.updateCourseById(courseId, dto);

        Assertions.assertNotNull(validCourse.getUpdatedAt());

        verify(courseRepository).findById(courseId);
        verify(modelMapper).map(dto, validCourse);
        verify(courseRepository).save(validCourse);
    }

    @Test
    void updateCourseById_ShouldThrowCourseNotFoundException_WhenCourseNotExists() {

        long courseId = 1L;

        CourseRequestDTO dto = new CourseRequestDTO();

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        CourseNotFoundException exception = Assertions.assertThrows(
                CourseNotFoundException.class
        , () -> courseService.updateCourseById(courseId,dto ));

        Assertions.assertEquals("Course not found : "+courseId, exception.getMessage());

        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(Course.class));
        verifyNoInteractions(modelMapper);

    }

    @Test
    void updateCourseById_ShouldThrowRuntimeException_WhenIdIsInvalid() {
        long courseId = 0L;
        CourseRequestDTO dto = new CourseRequestDTO();

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> courseService.updateCourseById(courseId, dto)
        );

        Assertions.assertEquals("Other type of exception", exception.getMessage());

        verifyNoInteractions(courseRepository);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void deleteCourseById_ShouldDeleteCourse_WhenCourseExists() {
        long courseId = 1L;
        Course validCourse = new Course();
        validCourse.setCourseName("Java");

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(validCourse));

        courseService.deleteCourseById(courseId);

        verify(courseRepository).findById(courseId);
        verify(courseRepository).deleteById(courseId);

    }


    @Test
    void deleteCourseById_ShouldThrowCourseNotFoundException_WhenCourseNotExists() {
        long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        CourseNotFoundException ex = Assertions.assertThrows(
                CourseNotFoundException.class,
                () -> courseService.deleteCourseById(courseId)
        );

        Assertions.assertEquals("Course not found : "+courseId, ex.getMessage());

        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteCourseById_ShouldThrowRuntimeException_WhenIdIsInvalid() {
        long courseId = 0L;

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> courseService.deleteCourseById(courseId)
        );

        Assertions.assertEquals("Other type of exception", exception.getMessage());

        verifyNoInteractions(courseRepository);
    }

    @Test
    void getCourses_ShouldReturnPaginatedCourses_WhenCoursesExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseCode("DS-128");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseCode("Java-177");

        CourseResponseDTO responseDTO1 = new CourseResponseDTO();
        responseDTO1.setId(1L);
        responseDTO1.setCourseCode("DS-128");

        CourseResponseDTO responseDTO2 = new CourseResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCourseCode("Java-177");

        List<Course> courseList = List.of(course1, course2);
        Page<Course> coursePage = new PageImpl<>(courseList, pageable, courseList.size());

        when(courseRepository.findAll(pageable)).thenReturn(coursePage);
        when(modelMapper.map(course1, CourseResponseDTO.class)).thenReturn(responseDTO1);
        when(modelMapper.map(course2, CourseResponseDTO.class)).thenReturn(responseDTO2);

        // Act
        Page<CourseResponseDTO> resultPage = courseService.getCourses(pageable);

        // Assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertEquals(2, resultPage.getTotalElements());
        Assertions.assertEquals("DS-128", resultPage.getContent().get(0).getCourseCode());
        Assertions.assertEquals("Java-177", resultPage.getContent().get(1).getCourseCode());
        Assertions.assertEquals(2, resultPage.getContent().size());
        Assertions.assertEquals(0, resultPage.getNumber());
        Assertions.assertEquals(10, resultPage.getSize());

        // Verify
        verify(courseRepository).findAll(pageable);
        verify(modelMapper).map(course1, CourseResponseDTO.class);
        verify(modelMapper).map(course2, CourseResponseDTO.class);
    }

    @Test
    void getCourses_ShouldReturnEmptyPage_WhenCoursesNotExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> emptyCoursePage = Page.empty(pageable);

        when(courseRepository.findAll(pageable)).thenReturn(emptyCoursePage);

        // Act
        Page<CourseResponseDTO> resultPage = courseService.getCourses(pageable);

        // Assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertTrue(resultPage.isEmpty());
        Assertions.assertEquals(0, resultPage.getTotalElements());

        // Verify
        verify(courseRepository).findAll(pageable);
        verifyNoInteractions(modelMapper);
    }

    @Test
    void searchCourses_ShouldReturnMatchingCourses_WhenSearchIsProvided() {
        // Arrange
        String search = "Java";
        Pageable pageable = PageRequest.of(0, 10);

        Course course = new Course();
        course.setId(1L);
        course.setCourseName("Advanced Java Programming");

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCourseName("Advanced Java Programming");

        List<Course> courseList = Collections.singletonList(course);
        Page<Course> mockCoursePage = new PageImpl<>(courseList, pageable, courseList.size());

        when(courseRepository.findByCourseNameContainingIgnoreCase(search, pageable))
                .thenReturn(mockCoursePage);
        when(modelMapper.map(course, CourseResponseDTO.class)).thenReturn(responseDTO);

        // Act
        Page<CourseResponseDTO> resultPage = courseService.searchCourses(search, pageable);

        // Assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertEquals(1, resultPage.getTotalElements());
        Assertions.assertEquals("Advanced Java Programming", resultPage.getContent().get(0).getCourseName());
        Assertions.assertEquals(1, resultPage.getContent().size());
        Assertions.assertFalse(resultPage.isEmpty());

        // Verify
        verify(courseRepository).findByCourseNameContainingIgnoreCase(search, pageable);
        verify(courseRepository, never()).findAll(any(Pageable.class));
        verify(modelMapper).map(course, CourseResponseDTO.class);
    }

    @Test
    void searchCourses_ShouldReturnAllCourses_WhenSearchIsNull() {
        // Arrange
        String search = null;
        Pageable pageable = PageRequest.of(0, 10);

        Course course = new Course();
        course.setId(2L);
        course.setCourseName("Data Structures & Algorithms");

        CourseResponseDTO responseDTO = new CourseResponseDTO();
        responseDTO.setId(2L);
        responseDTO.setCourseName("Data Structures & Algorithms");

        List<Course> courseList = Collections.singletonList(course);
        Page<Course> mockCoursePage = new PageImpl<>(courseList, pageable, courseList.size());

        when(courseRepository.findAll(pageable)).thenReturn(mockCoursePage);
        when(modelMapper.map(course, CourseResponseDTO.class)).thenReturn(responseDTO);

        // Act
        Page<CourseResponseDTO> resultPage = courseService.searchCourses(search, pageable);

        // Assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertEquals(1, resultPage.getTotalElements());
        Assertions.assertEquals("Data Structures & Algorithms", resultPage.getContent().get(0).getCourseName());

        // Verify
        verify(courseRepository).findAll(pageable);
        verify(courseRepository, never()).findByCourseNameContainingIgnoreCase(anyString(), any(Pageable.class));
        verify(modelMapper).map(course, CourseResponseDTO.class);
    }

    @Test
    void searchCourses_ShouldReturnEmptyPage_WhenNoMatchFound() {
        // Arrange
        String search = "NonExistentCourse";
        Pageable pageable = PageRequest.of(0, 10);

        Page<Course> emptyCoursePage = Page.empty(pageable);

        when(courseRepository.findByCourseNameContainingIgnoreCase(search, pageable))
                .thenReturn(emptyCoursePage);

        // Act
        Page<CourseResponseDTO> resultPage = courseService.searchCourses(search, pageable);

        // Assert
        Assertions.assertNotNull(resultPage);
        Assertions.assertTrue(resultPage.isEmpty());
        Assertions.assertEquals(0, resultPage.getTotalElements());

        // Verify
        verify(courseRepository).findByCourseNameContainingIgnoreCase(search, pageable);
        verify(courseRepository, never()).findAll(any(Pageable.class));
        verifyNoInteractions(modelMapper);
    }
















}
