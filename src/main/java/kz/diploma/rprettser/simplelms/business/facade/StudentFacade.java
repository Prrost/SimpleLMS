package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;

import java.util.List;

public interface StudentFacade {
    List<StudentResponseDto> getAllStudents();

    StudentResponseDto getStudentById(Long id);

    StudentResponseDto createStudent(StudentRequestDto studentRequestDto);

    StudentResponseDto updateStudent(Long id, StudentRequestDto studentRequestDto);

    StudentResponseDto deleteStudent(Long id);
}
