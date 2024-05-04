package dev.angelcruzl.controller;

import dev.angelcruzl.model.Student;
import dev.angelcruzl.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") long studentId) {
        return service.getStudentById(studentId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") long studentId,
                                                 @RequestBody Student updatedStudent) {
        return service.getStudentById(studentId)
                .map(student -> {
                    updatedStudent.setId(student.getId());
                    return ResponseEntity.ok(service.updateStudent(updatedStudent));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable("id") long studentId) {
        service.deleteStudent(studentId);
        return ResponseEntity.ok("Student with id " + studentId + " deleted successfully");
    }
}
