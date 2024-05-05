package dev.angelcruzl.service;

import dev.angelcruzl.exception.ResourceNotFoundException;
import dev.angelcruzl.model.Student;
import dev.angelcruzl.repository.StudentRepository;
import dev.angelcruzl.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentServiceImpl service;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .id(1L)
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
    }

    @DisplayName("JUnit test for save student operation")
    @Test
    public void givenStudentObject_whenSave_thenReturnSavedStudent() {
        // given - precondition or setup
        given(repository.findByEmail(student.getEmail())).willReturn(Optional.empty());
        given(repository.save(student)).willReturn(student);

        // when - action or the behaviour that we are going test
        Student savedStudent = service.createStudent(student);

        // then - verify the output
        assertThat(savedStudent).isNotNull();
    }

    @DisplayName("JUnit test for save student operation with existing email")
    @Test
    public void givenStudentObject_whenSaveWithExistingEmail_thenThrowException() {
        // given - precondition or setup
        given(repository.findByEmail(student.getEmail())).willReturn(Optional.of(student));

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> service.createStudent(student));

        // then - verify the output
        verify(repository, never()).save(any(Student.class));
    }

    @DisplayName("JUnit test for get all students operation")
    @Test
    public void givenStudentsList_whenFindAll_thenStudentsList() {
        // given - precondition or setup
        Student student2 = Student.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build();

        given(repository.findAll()).willReturn(java.util.List.of(student, student2));

        // when - action or the behaviour that we are going test
        java.util.List<Student> students = service.getAllStudents();

        // then - verify the output
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get all students operation when no students are found")
    @Test
    public void givenNoStudents_whenFindAll_thenReturnEmptyList() {
        // given - precondition or setup
        given(repository.findAll()).willReturn(java.util.List.of());
        given(repository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going test
        java.util.List<Student> students = service.getAllStudents();

        // then - verify the output
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(0);
    }

    @DisplayName("JUnit test for get student by id operation")
    @Test
    public void givenStudentId_whenFindById_thenReturnStudentObject() {
        // given - precondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.of(student));

        // when - action or the behaviour that we are going test
        Student studentDb = service.getStudentById(student.getId()).get();

        // then - verify the output
        assertThat(studentDb).isNotNull();
    }

    @DisplayName("JUnit test for update student operation")
    @Test
    public void givenStudentObject_whenUpdate_thenReturnUpdatedStudent() {
        // given - precondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.of(student));
        given(repository.save(student)).willReturn(student);
        student.setFirstName("Luis");
        student.setLastName("Lara");

        // when - action or the behaviour that we are going test
        Student updatedStudent = service.updateStudent(student);

        // then - verify the output
        assertThat(updatedStudent.getFirstName()).isEqualTo("Luis");
        assertThat(updatedStudent.getLastName()).isEqualTo("Lara");
    }

    @DisplayName("JUnit test for update student operation when student does not exist")
    @Test
    public void givenStudentObject_whenUpdate_thenThrowResourceNotFoundException() {
        // given - precondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> service.updateStudent(student));

        // then - verify the output
        verify(repository, never()).save(any(Student.class));
    }

    @DisplayName("JUnit test for delete student operation")
    @Test
    public void givenStudentId_whenDelete_thenVerifyDeleteIsCalled() {
        // given - precondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.of(student));

        // when - action or the behaviour that we are going test
        service.deleteStudent(student.getId());

        // then - verify the output
        verify(repository, times(1)).deleteById(student.getId());
    }

    @DisplayName("JUnit test for delete student operation when student does not exist")
    @Test
    public void givenStudentId_whenDelete_thenThrowResourceNotFoundException() {
        // given - precondition or setup
        given(repository.findById(student.getId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> service.deleteStudent(student.getId()));

        // then - verify the output
        verify(repository, never()).deleteById(student.getId());
    }

}
