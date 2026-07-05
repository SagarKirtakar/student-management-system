package com.sagar.sms.services;

import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.StudentNotFoundException;

import java.util.List;
import java.util.Optional;


public interface StudentService {

    public Student createStd(Student std);

    public List<Student> getAllStd();

    public Student getStdById(long id) throws StudentNotFoundException;

    public Student updateStdById(long id, Student std) throws StudentNotFoundException;

    public void deleteStdById(long id) throws StudentNotFoundException;
}
