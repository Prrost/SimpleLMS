package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentFacade {
    List<StudentResponseDto> getAllStudents();

    Page<StudentResponseDto> getAllStudentsPageable(Pageable pageable);

    Page<StudentResponseDto> searchStudents(String name, String lastName, String email, String phone, Pageable pageable);

    StudentResponseDto getStudentById(Long id);

    StudentResponseDto createStudent(StudentRequestDto studentRequestDto);

    StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto);

    StudentResponseDto deleteStudent(Long id);
}
