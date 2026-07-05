package com.sagar.sms.controller;

import com.sagar.sms.entity.Student;
import com.sagar.sms.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student createStudent(@RequestBody Student std) {
        return studentService.createStd(std);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStd();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStdById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student newStd ) {
        return ResponseEntity.ok(studentService.updateStdById(id, newStd));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
            studentService.deleteStdById(id);
            return ResponseEntity.ok("Student deleted successfully");
    }

}
