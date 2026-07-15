package com.sagar.sms.controller;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseRequestDTO requestDTO) {
        courseService.createCourse(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourse();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequestDTO courseRequestDTO ) {
        courseService.updateCourseById(id, courseRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CourseResponseDTO>> getCourses(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        if(pageNo < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal 1");
        }
        if(pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than or equal 1");
        }
        return ResponseEntity.ok(courseService.getCourses(PageRequest.of(pageNo-1, pageSize,sort)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponseDTO>> searchCourses(
            @RequestParam String courseName,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(pageNo - 1, pageSize, sort);

        return ResponseEntity.ok(
                courseService.searchCourses(courseName, pageable)
        );
    }

}
