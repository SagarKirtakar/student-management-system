package com.sagar.sms.service;

import com.sagar.sms.dto.StudentRequestDTO;
import com.sagar.sms.dto.StudentResponseDTO;
import com.sagar.sms.entity.Student;
import com.sagar.sms.exception.EmailAlreadyExistsException;
import com.sagar.sms.exception.StudentNotFoundException;
import com.sagar.sms.repository.StudentRepository;
import com.sagar.sms.services.StudentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void createStudent_ShouldSaveStudent_WhenEmailDoesNotExist() {

        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setFirstName("Sagar");
        requestDTO.setEmail("sagar@gmail.com");

        Student student = new Student();

        when(studentRepository.existsByEmail("sagar@gmail.com"))
                .thenReturn(false);

        when(modelMapper.map(requestDTO, Student.class))
                .thenReturn(student);

        studentService.createStd(requestDTO);

        verify(studentRepository).existsByEmail("sagar@gmail.com");
        verify(modelMapper).map(requestDTO, Student.class);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void createStudent_ShouldThrowException_WhenEmailAlreadyExists() {

        // Arrange
        StudentRequestDTO requestDTO = new StudentRequestDTO();
        requestDTO.setFirstName("Sagar");
        requestDTO.setEmail("sagar@gmail.com");

        when(studentRepository.existsByEmail("sagar@gmail.com"))
                .thenReturn(true);

        // Act & Assert
        EmailAlreadyExistsException ex = Assertions.assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.createStd(requestDTO)
        );
        Assertions.assertEquals("Email is already exists", ex.getMessage());

        // Verify
        verify(studentRepository).existsByEmail("sagar@gmail.com");
        verify(modelMapper, never()).map(any(), any());
        verify(studentRepository, never()).save(any(Student.class));

    }

    @Test
    void getStudentById_ShouldReturnStudent_WhenIdExists() {

        // Arrange
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");

        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setId(1L);
        studentResponseDTO.setFirstName("Sagar");

        when(studentRepository.findById(student.getId()))
                .thenReturn(Optional.of(student));

        when(modelMapper.map(student, StudentResponseDTO.class))
                .thenReturn(studentResponseDTO);

        // Act
        StudentResponseDTO studentResponseDTO1 = studentService.getStdById(1L);

        // Assert
        Assertions.assertEquals(1L, studentResponseDTO.getId());
        Assertions.assertEquals("Sagar", studentResponseDTO1.getFirstName());
        verify(studentRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(student, StudentResponseDTO.class);

    }

    @Test
    void getStudentById_ShouldThrowStudentNotFoundException_WhenIdNotExists() {

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        StudentNotFoundException ex  = Assertions.assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStdById(1L)
        );

        Assertions.assertEquals("Student not found : 1", ex.getMessage());

        verify(studentRepository, times(1)).findById(1L);
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getStudentById_ShouldThrowRuntimeException_WhenIdIsInvalid() {

       RuntimeException runtimeException  = Assertions.assertThrows(
               RuntimeException.class,
               () -> studentService.getStdById(0));

       Assertions.assertEquals("Other type of exception", runtimeException.getMessage());

       verify(studentRepository, never()).findById(anyLong());
       verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void updateStudent_ShouldUpdateStudent_WhenStudentExists() {

        Student valid = new Student();
        valid.setId(1L);
        valid.setFirstName("Sagar");
        valid.setLastName("Kirtakar");
        valid.setEmail("sagar@gmail.com");

        StudentRequestDTO studentRequestDTO = new StudentRequestDTO();
        studentRequestDTO.setFirstName("Sagar");
        studentRequestDTO.setLastName("Kirtakar");
        studentRequestDTO.setEmail("sagar2002@gmail.com");


        when(studentRepository.findById(1L)).thenReturn(Optional.of(valid));

        studentService.updateStdById(1L, studentRequestDTO);

        Assertions.assertNotNull(valid.getUpdatedAt());

        verify(studentRepository, times(1)).findById(1L);
        verify(modelMapper, times(1)).map(studentRequestDTO, valid);
        verify(studentRepository, times(1)).save(valid);

    }

    @Test
    void updateStudent_ShouldThrowStudentNotFoundException_WhenStudentNotExists() {

        // Arrange

        StudentRequestDTO studentRequestDTO = new StudentRequestDTO();

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentNotFoundException exception = Assertions.assertThrows(
                StudentNotFoundException.class
        , () -> studentService.updateStdById(1L, studentRequestDTO));

        Assertions.assertEquals("Student not found : 1", exception.getMessage());

        // Verify
        verify(studentRepository, times(1)).findById(1L);
        verify(modelMapper, never()).map(any(), any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudentById_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        StudentRequestDTO studentRequestDTO = new StudentRequestDTO();

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> studentService.updateStdById(0, studentRequestDTO ));

        Assertions.assertEquals("Other type of exception", exception.getMessage());

        verify(studentRepository, never()).save(any());
        verify(modelMapper, never()).map(any(), any());
        verify(studentRepository, never()).findById(anyLong());
    }

    @Test
    void deleteStudentById_ShouldDeleteStudent_WhenStudentExists() {

        // Arrange
        Student validStudent = new Student();
        validStudent.setId(1L);
        validStudent.setFirstName("Sagar");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(validStudent));

        // Act
        studentService.deleteStdById(1L);

        // Verify
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStudentById_ShouldThrowStudentNotFoundException_WhenStudentNotExists() {

        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());

        StudentNotFoundException exception = Assertions.assertThrows(
                StudentNotFoundException.class,
                ()-> studentService.deleteStdById(1L));

        Assertions.assertEquals(
                "Student not found : 1",
                exception.getMessage()
        );

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteStudentById_ShouldThrowRuntimeException_WhenIdIsInvalid() {

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> studentService.deleteStdById(0)
        );

        Assertions.assertEquals("Other type of exception", exception.getMessage());

        verify(studentRepository, never()).deleteById(anyLong());
        verify(studentRepository, never()).findById(anyLong());
    }

}