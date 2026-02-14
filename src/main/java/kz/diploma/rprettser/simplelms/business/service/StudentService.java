package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> getStudentById(Long id);
    List<Student> getAllStudents();
    Student createStudent(StudentRequestDto studentDto);
    Student updateStudent(Long id, StudentRequestDto studentDto);
    Student deleteStudent(Long id);
    Optional<Student> findStudentByStudentName(String studentName);
}
