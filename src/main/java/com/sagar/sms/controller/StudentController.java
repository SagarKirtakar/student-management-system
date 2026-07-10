package com.sagar.sms.controller;

import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
         studentService.createStd(studentRequestDTO);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStd();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStdById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDTO studentRequestDTO ) {
        studentService.updateStdById(id, studentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
            studentService.deleteStdById(id);
            return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/page")
    public ResponseEntity<Page<StudentResponseDTO>> getStudents(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        if(pageNo < 1) {
            throw new IllegalArgumentException("Page number must be greater than or equal 1");
        }
        return ResponseEntity.ok(studentService.getStudents(PageRequest.of(pageNo-1, pageSize)));
    }


}
