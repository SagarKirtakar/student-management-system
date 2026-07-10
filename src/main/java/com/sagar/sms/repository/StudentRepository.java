package com.sagar.sms.repository;

import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    Page<Student> findByFirstNameContainingIgnoreCase(String search, Pageable pageable);
}
