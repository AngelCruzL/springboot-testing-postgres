package dev.angelcruzl.service.impl;

import dev.angelcruzl.exception.ResourceNotFoundException;
import dev.angelcruzl.model.Student;
import dev.angelcruzl.repository.StudentRepository;
import dev.angelcruzl.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repository;

    @Override
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    @Override
    public Student createStudent(Student student) {
        Optional<Student> savedStudent = repository.findByEmail(student.getEmail());
        if (savedStudent.isPresent()) {
            throw new ResourceNotFoundException("Student with email " + student.getEmail() + " already exists");
        }

        return repository.save(student);
    }

    @Override
    public Optional<Student> getStudentById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Student updateStudent(Student student) {
        Optional<Student> savedStudent = repository.findById(student.getId());
        if (savedStudent.isEmpty()) {
            throw new ResourceNotFoundException("Student with id " + student.getId() + " not found");
        }

        return repository.save(student);
    }

    @Override
    public void deleteStudent(Long id) {
        Optional<Student> savedStudent = repository.findById(id);
        if (savedStudent.isEmpty()) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }

        repository.deleteById(id);
    }
}
