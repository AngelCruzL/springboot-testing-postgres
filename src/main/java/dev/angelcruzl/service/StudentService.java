package dev.angelcruzl.service;

import dev.angelcruzl.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();

    Student createStudent(Student student);

    Optional<Student> getStudentById(Long id);

    Student updateStudent(Student student);

    void deleteStudent(Long id);
}
