package com.sagar.sms.services;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.exception.CourseNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    void createCourse(CourseRequestDTO requestDTO);

    List<CourseResponseDTO> getAllCourse();

    CourseResponseDTO getCourseById(long id)
            throws CourseNotFoundException;

    void updateCourseById(
            long id,
            CourseRequestDTO requestDTO
    ) throws CourseNotFoundException;

    void deleteCourseById(long id)
            throws CourseNotFoundException;

    Page<CourseResponseDTO> getCourses(Pageable pageable);

    Page<CourseResponseDTO> searchCourses(
            String search,
            Pageable pageable
    );
}