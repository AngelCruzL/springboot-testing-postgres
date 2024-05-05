package dev.angelcruzl.repository;

import dev.angelcruzl.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class StudentRepositoryTests {

    @Autowired
    private StudentRepository repository;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
    }

    @DisplayName("JUnit test for save student operation")
    @Test
    public void givenStudentObject_whenSave_thenReturnSavedStudent() {
        // given - precondition or setup
        // when - action or the behaviour that we are going test
        Student savedStudent = repository.save(student);

        // then - verify the output
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isGreaterThan(0);
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

        repository.save(student);
        repository.save(student2);

        // when - action or the behaviour that we are going test
        List<Student> students = repository.findAll();

        // then - verify the output
        assertThat(students).isNotNull();
        assertThat(students.size()).isEqualTo(2);
    }

    @DisplayName("JUnit test for get student by id operation")
    @Test
    public void givenStudentId_whenFindById_thenReturnStudentObject() {
        // given - precondition or setup
        repository.save(student);

        // when - action or the behaviour that we are going test
        Student studentFound = repository.findById(student.getId()).orElse(null);

        // then - verify the output
        assertThat(studentFound).isNotNull();
        assertThat(studentFound.getId()).isEqualTo(student.getId());
        assertThat(studentFound.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(studentFound.getLastName()).isEqualTo(student.getLastName());
        assertThat(studentFound.getEmail()).isEqualTo(student.getEmail());
    }

    @DisplayName("JUnit test for get student by email operation")
    @Test
    public void givenStudentObject_whenFindByEmail_thenReturnStudentObject() {
        // given - precondition or setup
        repository.save(student);

        // when - action or the behaviour that we are going test
        Student studentFound = repository.findByEmail(student.getEmail()).orElse(null);

        // then - verify the output
        assertThat(studentFound).isNotNull();
        assertThat(studentFound.getId()).isEqualTo(student.getId());
        assertThat(studentFound.getFirstName()).isEqualTo(student.getFirstName());
        assertThat(studentFound.getLastName()).isEqualTo(student.getLastName());
        assertThat(studentFound.getEmail()).isEqualTo(student.getEmail());
    }

    @DisplayName("JUnit test for update student operation")
    @Test
    public void givenStudentObject_whenUpdate_thenReturnUpdatedStudent() {
        // given - precondition or setup
        repository.save(student);

        // when - action or the behaviour that we are going test
        Student savedStudent = repository.findById(student.getId()).get();
        savedStudent.setFirstName("John");
        savedStudent.setLastName("Doe");
        savedStudent.setEmail("john@doe");
        Student updatedStudent = repository.save(savedStudent);

        // then - verify the output
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getId()).isEqualTo(savedStudent.getId());
        assertThat(updatedStudent.getFirstName()).isEqualTo("John");
        assertThat(updatedStudent.getLastName()).isEqualTo("Doe");
        assertThat(updatedStudent.getEmail()).isEqualTo("john@doe");
    }

    @DisplayName("JUnit test for delete student operation")
    @Test
    public void givenStudentId_whenDelete_thenStudentDeleted() {
        // given - precondition or setup
        repository.save(student);

        // when - action or the behaviour that we are going test
        repository.deleteById(student.getId());

        // then - verify the output
        assertThat(repository.findById(student.getId())).isEmpty();
    }

}
