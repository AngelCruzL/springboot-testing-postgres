package dev.angelcruzl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angelcruzl.model.Student;
import dev.angelcruzl.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("JUnit test for create student operation")
    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnStudent() throws Exception {
        // given - precondition or setup
        Student student = Student.builder()
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();

        given(service.createStudent(any(Student.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

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

        given(service.getAllStudents()).willReturn(studentList);

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
                .id(1L)
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();
        given(service.getStudentById(student.getId())).willReturn(Optional.of(student));

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
        given(service.getStudentById(studentId)).willReturn(Optional.empty());

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
                .id(1L)
                .firstName("Angel")
                .lastName("Cruz")
                .email("me@angelcruzl.dev")
                .build();

        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Lara")
                .email("mail@sample.com")
                .build();

        given(service.getStudentById(student.getId())).willReturn(Optional.of(student));
        given(service.updateStudent(any(Student.class))).willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/v1/students/{id}", student.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id", is(updatedStudent.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(updatedStudent.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedStudent.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedStudent.getEmail())));
    }

    @DisplayName("JUnit test for update student operation with non-existing student id")
    @Test
    public void givenStudentIdAndUpdatedStudent_whenUpdateStudent_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        Student updatedStudent = Student.builder()
                .id(1L)
                .firstName("Luis")
                .lastName("Lara")
                .email("me@angelcruzl.dev")
                .build();

        given(service.getStudentById(studentId)).willReturn(Optional.empty());

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
        long studentId = 1L;
        willDoNothing().given(service).deleteStudent(studentId);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/v1/students/{id}", studentId));

        // then - verify the output
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Student with id " + studentId + " deleted successfully")));
    }
}
