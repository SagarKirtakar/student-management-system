package com.sagar.sms.controller;

import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.services.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@Tag(name = "Student Management", description = "APIs for managing student records")
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Operation(summary = "Create Student", description = "Creates a new student in the system.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Student created successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data")})
    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        studentService.createStd(studentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get All Students", description = "Returns a list of all students.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Students retrieved successfully")})
    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStd();
    }

    @Operation(summary = "Get Student by ID", description = "Retrieves a student using the given student ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student found"), @ApiResponse(responseCode = "404", description = "Student not found")})
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable @Parameter(description = "Student ID", example = "1") Long id) {
        return ResponseEntity.ok(studentService.getStdById(id));
    }

    @Operation(summary = "Update Student", description = "Updates an existing student's information.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Student updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data"), @ApiResponse(responseCode = "404", description = "Student not found")})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@Parameter(description = "Student ID", example = "1") @PathVariable Long id, @Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        studentService.updateStdById(id, studentRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Student", description = "Deletes a student using the given ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Student deleted successfully"), @ApiResponse(responseCode = "404", description = "Student not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@Parameter(description = "Student ID", example = "1") @PathVariable Long id) {
        studentService.deleteStdById(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @Operation(summary = "Get Students with Pagination", description = "Returns students with pagination and sorting support.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Students retrieved successfully")})
    @GetMapping("/page")
    public ResponseEntity<Page<StudentResponseDTO>> getStudents(@Parameter(description = "Page Number", example = "1") @RequestParam(defaultValue = "1") int pageNo,

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
        return ResponseEntity.ok(studentService.getStudents(PageRequest.of(pageNo - 1, pageSize, sort)));
    }

    @Operation(summary = "Search Students", description = "Search students by first name.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Students retrieved successfully")})
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponseDTO>> searchStudents(@RequestParam @Parameter(description = "Student first name", example = "Sagar") String firstName, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "5") int pageSize, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        return ResponseEntity.ok(studentService.searchStudents(firstName, pageable));
    }


}
