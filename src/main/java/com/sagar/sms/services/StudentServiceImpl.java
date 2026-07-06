package com.sagar.sms.services;

import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.StudentNotFoundException;
import com.sagar.sms.repository.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService{

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createStd(StudentRequestDTO studentRequestDTO) {
       Student std =  modelMapper.map(studentRequestDTO, Student.class);
       std.setCreatedAt(LocalDateTime.now());
       std.setUpdatedAt(LocalDateTime.now());
       studentRepository.save(std);
    }

    @Override
    public List<StudentResponseDTO> getAllStd() {
        List<Student> studentList = studentRepository.findAll();
        List<StudentResponseDTO> studentResponseDTOS = studentList.stream()
                .map(std -> modelMapper.map(std, StudentResponseDTO.class))
                .toList();
        return studentResponseDTOS;
    }

    @Override
    public StudentResponseDTO getStdById(long id) throws StudentNotFoundException {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Optional<Student> optional = studentRepository.findById(id);
        if(optional.isPresent()) {
            Student std = optional.get();
           return modelMapper.map(std, StudentResponseDTO.class);
        }else {
            throw new StudentNotFoundException("Student not found : "+id);
        }

    }

    @Override
    public void updateStdById(long id, StudentRequestDTO stdRequestDTO) throws StudentNotFoundException {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
        Student validStd = studentRepository.findById(id)
                .orElseThrow(() ->  new StudentNotFoundException("Student not found : "+id));

        modelMapper.map(stdRequestDTO, validStd);
        validStd.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(validStd);
    }

    @Override
    public void deleteStdById(long id) throws StudentNotFoundException {
        if(id <= 0) {
            throw new RuntimeException("Other type of exception");
        }
       Optional<Student> optional  = studentRepository.findById(id);
        if(optional.isPresent()) {
            studentRepository.deleteById(id);
        }else {
            throw new StudentNotFoundException("Student not found : "+id);
        }
    }

}
