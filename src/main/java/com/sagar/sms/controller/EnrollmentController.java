package com.sagar.sms.controller;

import com.sagar.sms.dto.EnrollmentRequestDTO;
import com.sagar.sms.dto.EnrollmentResponseDTO;
import com.sagar.sms.dto.GradeRequestDTO;
import com.sagar.sms.services.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollment Management", description = "APIs for student enrollments and grade assignment")
@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Operation(summary = "Create Enrollment", description = "Enrolls a student into a course.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Enrollment created successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data"), @ApiResponse(responseCode = "404", description = "Student or Course not found")})
    @PostMapping
    public ResponseEntity<Void> createEnrollment(@Valid @RequestBody EnrollmentRequestDTO requestDTO) {

        enrollmentService.createEnrollment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Get All Enrollments", description = "Returns a list of all student enrollments.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")})
    @GetMapping
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @Operation(summary = "Get Enrollment by ID", description = "Retrieves an enrollment using the given enrollment ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Enrollment found"), @ApiResponse(responseCode = "404", description = "Enrollment not found")})
    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(

            @Parameter(description = "Enrollment ID", example = "1") @PathVariable Long id) {

        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    @Operation(summary = "Update Enrollment", description = "Updates an existing enrollment.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Enrollment updated successfully"), @ApiResponse(responseCode = "400", description = "Invalid request data"), @ApiResponse(responseCode = "404", description = "Enrollment not found")})
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEnrollment(

            @Parameter(description = "Enrollment ID", example = "1") @PathVariable Long id,

            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {

        enrollmentService.updateEnrollmentById(id, requestDTO);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete Enrollment", description = "Deletes an enrollment using the given enrollment ID.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Enrollment deleted successfully"), @ApiResponse(responseCode = "404", description = "Enrollment not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnrollment(

            @Parameter(description = "Enrollment ID", example = "1") @PathVariable Long id) {

        enrollmentService.deleteEnrollmentById(id);

        return ResponseEntity.ok("Enrollment deleted successfully");
    }

    @Operation(summary = "Get Enrollments with Pagination", description = "Returns enrollments with pagination and sorting support.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")})
    @GetMapping("/page")
    public ResponseEntity<Page<EnrollmentResponseDTO>> getEnrollments(

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

        return ResponseEntity.ok(enrollmentService.getEnrollments(PageRequest.of(pageNo - 1, pageSize, sort)));
    }

    @Operation(summary = "Search Enrollments", description = "Searches enrollments by enrollment status.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")})
    @GetMapping("/search")
    public ResponseEntity<Page<EnrollmentResponseDTO>> searchEnrollments(

            @Parameter(description = "Enrollment Status", example = "ACTIVE") @RequestParam String status,

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

        return ResponseEntity.ok(enrollmentService.searchEnrollments(status, pageable));
    }

    @Operation(summary = "Assign Grade", description = "Assigns or updates a grade and remarks for an active enrollment.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Grade assigned successfully"), @ApiResponse(responseCode = "400", description = "Invalid grade or enrollment is not ACTIVE"), @ApiResponse(responseCode = "404", description = "Enrollment not found")})
    @PutMapping("/{id}/grade")
    public ResponseEntity<Void> assignGrade(

            @Parameter(description = "Enrollment ID", example = "1") @PathVariable Long id,

            @Valid @RequestBody GradeRequestDTO requestDTO) {

        enrollmentService.assignGrade(id, requestDTO);

        return ResponseEntity.noContent().build();
    }
}