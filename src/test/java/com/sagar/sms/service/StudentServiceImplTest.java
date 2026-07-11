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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    void getAllStudents_ShouldReturnStudentList_WhenStudentsExist() {

        // Arrange
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("Sagar");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Rahul");

        StudentResponseDTO dto1 = new StudentResponseDTO();
        dto1.setId(1L);
        dto1.setFirstName("Sagar");

        StudentResponseDTO dto2 = new StudentResponseDTO();
        dto2.setId(2L);
        dto2.setFirstName("Rahul");

        when(studentRepository.findAll())
                .thenReturn(List.of(student1, student2));
        when(modelMapper.map(student1, StudentResponseDTO.class))
                .thenReturn(dto1);
        when(modelMapper.map(student2, StudentResponseDTO.class))
                .thenReturn(dto2);

        // Act
        List<StudentResponseDTO> result =  studentService.getAllStd();
        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                "Sagar",
                result.get(0).getFirstName());
        Assertions.assertEquals(
                "Rahul",
                result.get(1).getFirstName());
        Assertions.assertEquals(
                2,
                result.size());

        // Verify
        verify(studentRepository, times(1)).findAll();
        verify(modelMapper, times(2))
                .map(any(Student.class), eq(StudentResponseDTO.class));
    }

    @Test
    void getAllStudents_ShouldReturnEmptyList_WhenStudentsNotExist() {

        when(studentRepository.findAll())
                .thenReturn(List.of());

        List<StudentResponseDTO> result = studentService.getAllStd();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.size());

        verify(studentRepository, times(1)).findAll();
        verify(modelMapper, never()).map(any(), any());

    }

    @Test
    void getStudents_ShouldReturnPaginatedStudents_WhenStudentsExist() {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("Sagar");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Rahul");

        StudentResponseDTO dto1 = new StudentResponseDTO();
        dto1.setId(1L);
        dto1.setFirstName("Sagar");

        StudentResponseDTO dto2 = new StudentResponseDTO();
        dto2.setId(2L);
        dto2.setFirstName("Rahul");

        Pageable pageable = PageRequest.of(1, 5);

        Page<Student> studentPage = new PageImpl<>(List.of(student1, student2));

        when(studentRepository.findAll(pageable)).thenReturn(studentPage);
        when(modelMapper.map(student1, StudentResponseDTO.class)).thenReturn(dto1);
        when(modelMapper.map(student2, StudentResponseDTO.class)).thenReturn(dto2);

        Page<StudentResponseDTO> result = studentService.getStudents(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals("Sagar", result.getContent().get(0).getFirstName());
        Assertions.assertEquals("Rahul", result.getContent().get(1).getFirstName());

        verify(studentRepository, times(1)).findAll(pageable);
        verify(modelMapper, times(2)).map(any(Student.class), eq(StudentResponseDTO.class));
    }

    @Test
    void getStudents_ShouldReturnEmptyPage_WhenStudentsNotExist() {

        // Arrange
        Pageable pageable = PageRequest.of(1, 5);

        when(studentRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));
        // Act
        Page<StudentResponseDTO> result =
                studentService.getStudents(pageable);
        // Assert
        Assertions.assertEquals(0, result.getContent().size());
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(1, result.getNumber());
        Assertions.assertEquals(5, result.getSize());

        // Verify
        verify(studentRepository, times(1)).findAll(pageable);
        verify(modelMapper, never()).map(any(), any());

    }

    @Test
    void searchStudents_ShouldReturnMatchingStudents_WhenSearchIsProvided() {

        // Arrange
        String search = "Sagar";
        Pageable pageable = PageRequest.of(0, 5);

        Student student = new Student();
        student.setId(1L);
        student.setFirstName("Sagar");

        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(1L);
        dto.setFirstName("Sagar");

        Page<Student> studentPage = new PageImpl<>(
                List.of(student),
                pageable,
                1
        );

        when(studentRepository
                .findByFirstNameContainingIgnoreCase(search, pageable))
                .thenReturn(studentPage);

        when(modelMapper.map(student, StudentResponseDTO.class))
                .thenReturn(dto);

        // Act
        Page<StudentResponseDTO> result =
                studentService.searchStudents(search, pageable);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(
                "Sagar",
                result.getContent().getFirst().getFirstName()
        );

        // Verify
        verify(studentRepository, times(1))
                .findByFirstNameContainingIgnoreCase(search, pageable);

        verify(modelMapper, times(1))
                .map(student, StudentResponseDTO.class);

        verify(studentRepository, never())
                .findAll(any(Pageable.class));
    }

    @Test
    void searchStudents_ShouldReturnAllStudents_WhenSearchIsNull() {

        // Arrange
        Pageable pageable = PageRequest.of(1, 5);
        String search = null;

        Page<Student> studentPage = Page.empty(pageable);

        when(studentRepository.findAll(pageable))
                .thenReturn(studentPage);

        // Act
        Page<StudentResponseDTO> result =
                studentService.searchStudents(search, pageable);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getContent().size());

        // Verify
        verify(studentRepository, times(1))
                .findAll(pageable);

        verify(studentRepository, never())
                .findByFirstNameContainingIgnoreCase(
                        anyString(),
                        any(Pageable.class)
                );

        verify(modelMapper, never())
                .map(any(), any());
    }

    @Test
    void searchStudents_ShouldReturnEmptyPage_WhenNoMatchFound() {

        // Arrange
        String search = "UnKnown";

        Pageable pageable = PageRequest.of(1, 5);

        Page<Student> studentPage = Page.empty(pageable);

        when(studentRepository
                .findByFirstNameContainingIgnoreCase(search, pageable))
                .thenReturn(studentPage);

        // Act
        Page<StudentResponseDTO> result =
                studentService.searchStudents(search, pageable);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
        Assertions.assertEquals(0, result.getTotalElements());

        // Verify
        verify(studentRepository, times(1))
                .findByFirstNameContainingIgnoreCase(search, pageable);

        verify(studentRepository, never())
                .findAll(any(Pageable.class));

        verify(modelMapper, never())
                .map(any(), any());
    }

}