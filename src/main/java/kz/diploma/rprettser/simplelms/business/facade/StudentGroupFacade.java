package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;

import java.util.List;

public interface StudentGroupFacade {
    List<StudentGroupResponseDto> getAllStudentGroups();

    StudentGroupResponseDto getStudentGroupById(Long id);

    StudentGroupResponseDto createStudentGroup(StudentGroupRequestDto studentGroupRequestDto);

    StudentGroupResponseDto updateStudentGroup(Long id, StudentGroupRequestDto studentGroupRequestDto);

    StudentGroupResponseDto deleteStudentGroup(Long id);
}
