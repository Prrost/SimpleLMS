package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class StudentFacadeImpl implements StudentFacade {

    private final StudentService studentService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<StudentResponseDto> getAllStudents() {
        List<Student> students =  studentService.getAllStudents();

        return mapper.toListStudentResponseDto(students);
    }

    @Override
    @Transactional
    public Page<StudentResponseDto> searchStudents(String name, String lastName, String email, String phone, Pageable pageable){
        return mapper.toPageableGroupResponseDto(
                studentService.searchStudents(name, lastName, email, phone, pageable)
        );
    }

    @Override
    @Transactional
    public Page<StudentResponseDto> getAllStudentsPageable(Pageable pageable) {
        Page<Student> students = studentService.getAllStudentsPageable(pageable);

        return students.map(mapper::toStudentResponseDto);
    }

    @Override
    @Transactional
    public StudentResponseDto getStudentById(Long id) {
        Student student = studentService.getStudentById(id).orElseThrow(() -> new NoSuchElementException("No student found with id: " + id));

        return mapper.toStudentResponseDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto studentRequestDto) {
        Student student = studentService.createStudent(studentRequestDto);

        return mapper.toStudentResponseDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto) {
        Student student = studentService.updateStudent(id, studentRequestDto);

        return mapper.toStudentResponseDto(student);
    }

    @Override
    @Transactional
    public StudentResponseDto deleteStudent(Long id) {
        Student student = studentService.deleteStudent(id);

        return mapper.toStudentResponseDto(student);
    }
}
