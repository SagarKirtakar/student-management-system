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
    public Student add(@RequestBody Student std) {
        return studentService.createStd(std);
    }

    @GetMapping
    public List<Student> getALl() {
        return studentService.getAllStd();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStdDetails(@PathVariable long id) {
        Student validStd = studentService.getStdById(id).orElse(null);
        if(validStd != null) {
            return ResponseEntity.ok().body(validStd);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStd(@PathVariable long id, @RequestBody Student newStd ) {
        Student validStd = studentService.updateStdById(id, newStd);
        if(validStd != null) {
            return ResponseEntity.ok().body(newStd);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStd(@PathVariable long id) {
        Student validStd = studentService.getStdById(id).orElse(null);
        if(validStd != null) {
            studentService.deleteStdById(id);
            return ResponseEntity.ok().body("User deleted successfully");
        }else {
            return ResponseEntity.ok().body("User not delete due to some error");
        }
    }

}
