package com.sagar.sms.services;

import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.StudentNotFoundException;
import com.sagar.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student createStd(Student std) {
        std.setCreatedAt(LocalDateTime.now());
        std.setUpdatedAt(LocalDateTime.now());
       return studentRepository.save(std);
    }

    @Override
    public List<Student> getAllStd() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStdById(long id) {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Student> optional = studentRepository.findById(id);
        if(optional.isPresent()) {
            return optional.get();
        }else {
            throw new StudentNotFoundException("Student not found "+id);
        }

    }

    @Override
    public Student updateStdById(long id, Student std) {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Student> optional = studentRepository.findById(id);
        if(optional.isPresent()) {
            Student existing = optional.get();
            existing.setFirstName(std.getFirstName());
            existing.setLastName(std.getLastName());
            existing.setEmail(std.getEmail());
            existing.setPhone(std.getPhone());
            existing.setCreatedAt(LocalDateTime.now());
            existing.setUpdatedAt(LocalDateTime.now());
            return studentRepository.save(existing);
        }else {
            throw new StudentNotFoundException("Student not found : "+id);
        }

    }

    @Override
    public void deleteStdById(long id) {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Student> optional = studentRepository.findById(id);
        if(optional.isPresent()) {
            studentRepository.deleteById(id);
        }else {
            throw new StudentNotFoundException("Student not found : "+id);
        }


    }
}
