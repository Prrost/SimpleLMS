package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentGroupFacade {
    List<StudentGroupResponseDto> getAllStudentGroups();

    Page<StudentGroupResponseDto> searchStudentGroups(String name, Boolean isVirtual, Pageable pageable);

    Page<StudentGroupResponseDto> getAllStudentGroupsPageable(Pageable pageable);

    StudentGroupResponseDto getStudentGroupById(Long id);

    StudentGroupResponseDto createStudentGroup(StudentGroupRequestDto studentGroupRequestDto);

    StudentGroupResponseDto updateStudentGroup(Long id, StudentGroupRequestDto studentGroupRequestDto);

    StudentGroupResponseDto deleteStudentGroup(Long id);
}
