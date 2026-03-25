package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Optional<Student> getStudentById(Long id);
    List<Student> getAllStudents();
    Page<Student> getAllStudentsPageable(Pageable pageable);
    Page<Student> searchStudents(String name, String lastName, String email, String phone, Pageable pageable);
    List<Student> getAllStudentsByIds(List<Long> ids);
    Student createStudent(StudentRequestDto studentDto);
    Student updateStudent(Long id, StudentRequestDto studentDto);
    Student deleteStudent(Long id);
    Optional<Student> findStudentByStudentName(String studentName);
}
