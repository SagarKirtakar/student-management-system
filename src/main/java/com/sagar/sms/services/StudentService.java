package com.sagar.sms.services;

import com.sagar.sms.entity.Student;
import java.util.List;
import java.util.Optional;


public interface StudentService {

    public Student createStd(Student std);

    public List<Student> getAllStd();

    public Optional<Student> getStdById(long id);

    public Student updateStdById(long id, Student std);

    public void deleteStdById(long id);
}
