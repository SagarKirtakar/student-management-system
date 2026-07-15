package com.sagar.sms.repository;

import com.sagar.sms.entity.Course;
import com.sagar.sms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCourseCode(String courseCode);

    Page<Course> findByCourseNameContainingIgnoreCase(
            String courseName,
            Pageable pageable
    );
}