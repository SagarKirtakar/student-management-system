package com.sagar.sms.controller;

import com.sagar.sms.dto.CourseRequestDTO;
import com.sagar.sms.dto.CourseResponseDTO;
import com.sagar.sms.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Course Management", description = "APIs for managing courses")
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Create Course", description = "Creates a new course in the system.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Course created successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data")})
    @PostMapping
    public ResponseEntity<Void> createCourse(@Valid @RequestBody CourseRequestDTO requestDTO) {

        courseService.createCourse(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get All Courses", description = "Returns a list of all available courses.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Courses retrieved successfully")})
    @GetMapping
    public List<CourseResponseDTO> getAllCourses() {
        return courseService.getAllCourse();
    }

    @Operation(summary = "Get Course by ID", description = "Retrieves a course using the given course ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Course found"), @ApiResponse(responseCode = "404", description = "Course not found")})
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(

            @Parameter(description = "Course ID", example = "1") @PathVariable Long id) {

        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @Operation(summary = "Update Course", description = "Updates an existing course.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Course updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data"), @ApiResponse(responseCode = "404", description = "Course not found")})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(

            @Parameter(description = "Course ID", example = "1") @PathVariable Long id,

            @Valid @RequestBody CourseRequestDTO courseRequestDTO) {

        courseService.updateCourseById(id, courseRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Course", description = "Deletes a course using the given course ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Course deleted successfully"), @ApiResponse(responseCode = "404", description = "Course not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(

            @Parameter(description = "Course ID", example = "1") @PathVariable Long id) {

        courseService.deleteCourseById(id);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @Operation(summary = "Get Courses with Pagination", description = "Returns courses with pagination and sorting support.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Courses retrieved successfully")})
    @GetMapping("/page")
    public ResponseEntity<Page<CourseResponseDTO>> getCourses(

            @Parameter(description = "Page Number", example = "1") @RequestParam(defaultValue = "1") int pageNo,

            @Parameter(description = "Page Size", example = "5") @RequestParam(defaultValue = "5") int pageSize,

            @Parameter(description = "Sort Field", example = "id") @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort Direction", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal 1");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than or equal 1");
        }

        return ResponseEntity.ok(courseService.getCourses(PageRequest.of(pageNo - 1, pageSize, sort)));
    }

    @Operation(summary = "Search Courses", description = "Searches courses by course name.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Courses retrieved successfully")})
    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponseDTO>> searchCourses(

            @Parameter(description = "Course Name", example = "Java Programming") @RequestParam String courseName,

            @Parameter(description = "Page Number", example = "1") @RequestParam(defaultValue = "1") int pageNo,

            @Parameter(description = "Page Size", example = "5") @RequestParam(defaultValue = "5") int pageSize,

            @Parameter(description = "Sort Field", example = "id") @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sort Direction", example = "ASC") @RequestParam(defaultValue = "ASC") String sortDir) {

        if (pageNo < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal 1");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must be greater than or equal 1");
        }

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return ResponseEntity.ok(courseService.searchCourses(courseName, pageable));
    }
}