package com.sagar.sms.services;

import com.sagar.sms.entity.Student;
import com.sagar.sms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student createStd(Student std) {
       return studentRepository.save(std);
    }

    @Override
    public List<Student> getAllStd() {
        return studentRepository.findAll();
    }

    @Override
    public Optional<Student> getStdById(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public Student updateStdById(long id, Student std) {
        Student validStd = studentRepository.findById(id).orElse(null);
        if(validStd != null) {
            return studentRepository.save(std);
        }else {
            throw new RuntimeException("Student not found : "+id);
        }

    }

    @Override
    public void deleteStdById(long id) {
        Student validStd = studentRepository.findById(id).orElse(null);
        if(validStd != null) {
            studentRepository.deleteById(id);
        }else {
            throw new RuntimeException("Student not found : "+id);
        }


    }
}
