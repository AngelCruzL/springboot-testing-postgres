package dev.angelcruzl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angelcruzl.model.Student;
import dev.angelcruzl.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class StudentControllerITests extends AbstractionContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    @DisplayName("JUnit test for create student operation")
    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnStudent() throws Exception {
        // given - precondition or setup
        Student student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @DisplayName("JUnit test for get all students operation")
    @Test
    public void givenStudentsList_whenFindAll_thenStudentsList() throws Exception {
        // given - precondition or setup
        List<Student> studentList = new ArrayList<>();
        studentList.add(Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build());
        studentList.add(Student.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe")
                .build());
        repository.saveAll(studentList);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/students"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(studentList.size())));
    }

    @DisplayName("JUnit test for get student by id operation")
    @Test
    public void givenStudentId_whenFindById_thenReturnStudentObject() throws Exception {
        // given - precondition or setup
        Student student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
        repository.save(student);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/students/{id}", student.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(student.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(student.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(student.getLastName())))
                .andExpect(jsonPath("$.email", is(student.getEmail())));
    }

    @DisplayName("JUnit test for get student by id operation with non-existing student id")
    @Test
    public void givenStudentId_whenFindById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long studentId = 1L;

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/students/{id}", studentId));

        // then - verify the output
        response.andExpect(status().isNotFound());
    }

    @DisplayName("JUnit test for update student operation")
    @Test
    public void givenStudentIdAndUpdatedStudent_whenUpdateStudent_thenReturnUpdatedStudent() throws Exception {
        // given - precondition or setup
        Student student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
        repository.save(student);

        Student updatedStudent = Student.builder()
                .firstName("Luis")
                .lastName("Lara")
                .email("mail@sample.com")
                .build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/students/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedStudent.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedStudent.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedStudent.getEmail())));
    }

    @DisplayName("JUnit test for update student operation with non-existing student id")
    @Test
    public void givenStudentIdAndUpdatedStudent_whenUpdateStudent_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long studentId = 404L;
        Student updatedStudent = Student.builder()
                .firstName("Luis")
                .lastName("Lara")
                .email("me@angelcruzl.dev")
                .build();
        repository.save(updatedStudent);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("JUnit test for delete student operation")
    @Test
    public void givenStudentId_whenDeleteStudent_thenReturnSuccessMessage() throws Exception {
        // given - precondition or setup
        Student student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
        repository.save(student);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/students/{id}", student.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Student with id " + student.getId() + " deleted successfully")));
    }
}
