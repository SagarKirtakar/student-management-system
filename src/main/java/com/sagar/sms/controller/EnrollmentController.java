package com.sagar.sms.controller;

import com.sagar.sms.dto.EnrollmentRequestDTO;
import com.sagar.sms.dto.EnrollmentResponseDTO;
import com.sagar.sms.dto.GradeRequestDTO;
import com.sagar.sms.services.EnrollmentService;
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

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Void> createEnrollment(
            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {

        enrollmentService.createEnrollment(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<EnrollmentResponseDTO> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enrollmentService.getEnrollmentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequestDTO requestDTO) {

        enrollmentService.updateEnrollmentById(id, requestDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEnrollment(
            @PathVariable Long id) {

        enrollmentService.deleteEnrollmentById(id);

        return ResponseEntity.ok("Enrollment deleted successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<EnrollmentResponseDTO>> getEnrollments(

            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        if (pageNo < 1) {
            throw new IllegalArgumentException(
                    "Page number must be greater than or equal 1");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException(
                    "Page size must be greater than or equal 1");
        }

        return ResponseEntity.ok(
                enrollmentService.getEnrollments(
                        PageRequest.of(pageNo - 1, pageSize, sort)
                )
        );
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EnrollmentResponseDTO>> searchEnrollments(

            @RequestParam String status,

            @RequestParam(defaultValue = "1") int pageNo,

            @RequestParam(defaultValue = "5") int pageSize,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "ASC") String sortDir) {

        if (pageNo < 1) {
            throw new IllegalArgumentException(
                    "Page number must be greater than or equal 1");
        }

        if (pageSize < 1) {
            throw new IllegalArgumentException(
                    "Page size must be greater than or equal 1");
        }

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable =
                PageRequest.of(pageNo - 1, pageSize, sort);

        return ResponseEntity.ok(
                enrollmentService.searchEnrollments(status, pageable)
        );
    }

    @PutMapping("/{id}/grade")
    public ResponseEntity<Void> assignGrade(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequestDTO requestDTO) {

        enrollmentService.assignGrade(id, requestDTO);

        return ResponseEntity.noContent().build();
    }
}
