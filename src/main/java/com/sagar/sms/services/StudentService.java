package com.sagar.sms.services;

import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.StudentNotFoundException;

import java.util.List;
import java.util.Optional;


public interface StudentService {

    public void createStd(StudentRequestDTO std);

    public List<StudentResponseDTO> getAllStd();

    public StudentResponseDTO getStdById(long id) throws StudentNotFoundException;

    public void updateStdById(long id, StudentRequestDTO std) throws StudentNotFoundException;

    public void deleteStdById(long id) throws StudentNotFoundException;
}
