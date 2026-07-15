package com.sagar.sms.services;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.entity.Course;
import com.sagar.sms.exception.CourseCodeAlreadyExistsException;
import com.sagar.sms.exception.CourseNotFoundException;
import com.sagar.sms.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void createCourse(CourseRequestDTO requestDTO) {
        if (courseRepository.existsByCourseCode(
                requestDTO.getCourseCode())) {

            throw new CourseCodeAlreadyExistsException(
                    "Course code already exists"
            );
        }

        Course course =
                modelMapper.map(requestDTO, Course.class);

        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        courseRepository.save(course);
    }

    @Override
    public List<CourseResponseDTO> getAllCourse() {
       List<Course> courseList =  courseRepository.findAll();
       List<CourseResponseDTO> responseDTOList = courseList
               .stream()
               .map(course -> modelMapper.map(course, CourseResponseDTO.class))
               .toList();

        return responseDTOList;
    }

    @Override
    public CourseResponseDTO getCourseById(long id) throws CourseNotFoundException {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Course> optional = courseRepository.findById(id);
        if(optional.isPresent()) {
            Course course = optional.get();
            return modelMapper.map(course, CourseResponseDTO.class);
        }else {
            throw new CourseNotFoundException("Course not found : "+id);
        }
    }

    @Override
    public void updateCourseById(long id, CourseRequestDTO requestDTO) throws CourseNotFoundException {

        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Course validCourse = courseRepository.findById(id)
                .orElseThrow(() ->  new CourseNotFoundException("Course not found : "+id));

        modelMapper.map(requestDTO, validCourse);
        validCourse.setUpdatedAt(LocalDateTime.now());
        courseRepository.save(validCourse);
    }

    @Override
    public void deleteCourseById(long id) throws CourseNotFoundException {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Course> optional  = courseRepository.findById(id);
        if(optional.isPresent()) {
            courseRepository.deleteById(id);
        }else {
            throw new CourseNotFoundException("Course not found : "+id);
        }
    }

    @Override
    public Page<CourseResponseDTO> getCourses(Pageable pageable) {
        Page<Course> coursePage =  courseRepository.findAll(pageable);
        return coursePage.map(course -> modelMapper.map(course, CourseResponseDTO.class));

    }

    @Override
    public Page<CourseResponseDTO> searchCourses(String search, Pageable pageable) {
        if (search != null) {
            Page<Course> coursePage =  courseRepository.findByCourseNameContainingIgnoreCase(search, pageable);
            return coursePage.map(course -> modelMapper.map(course, CourseResponseDTO.class));
        }else {
            Page<Course> coursePage =  courseRepository.findAll(pageable);
            return coursePage.map(course -> modelMapper.map(course, CourseResponseDTO.class));
        }
    }

}
