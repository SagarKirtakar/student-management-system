package com.sagar.sms.service;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.entity.Course;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.CourseCodeAlreadyExistsException;
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

import java.time.LocalDateTime;

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

        CourseRequestDTO requestDTO= new CourseRequestDTO();
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

        when(courseRepository.existsByCourseCode("DS-128")).thenThrow(CourseCodeAlreadyExistsException.class,
                )
    }

}
