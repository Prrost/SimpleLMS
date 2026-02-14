package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentGroupFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class StudentGroupFacadeImpl implements StudentGroupFacade {

    private final StudentGroupService studentGroupService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<StudentGroupResponseDto> getAllStudentGroups() {
        List<StudentGroup> studentGroups = studentGroupService.getAllStudentGroups();

        return mapper.toListStudentGroupResponseDto(studentGroups);
    }

    @Override
    @Transactional
    public StudentGroupResponseDto getStudentGroupById(Long id) {
        StudentGroup studentGroup = studentGroupService.getStudentGroupById(id)
                .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + id));

        return mapper.toStudentGroupResponseDto(studentGroup);
    }

    @Override
    @Transactional
    public StudentGroupResponseDto createStudentGroup(StudentGroupRequestDto studentGroupRequestDto) {
        StudentGroup studentGroup = studentGroupService.createStudentGroup(studentGroupRequestDto);

        return mapper.toStudentGroupResponseDto(studentGroup);
    }

    @Override
    @Transactional
    public StudentGroupResponseDto updateStudentGroup(Long id, StudentGroupRequestDto studentGroupRequestDto) {
        StudentGroup studentGroup = studentGroupService.updateStudentGroup(id, studentGroupRequestDto);

        return mapper.toStudentGroupResponseDto(studentGroup);
    }

    @Override
    @Transactional
    public StudentGroupResponseDto deleteStudentGroup(Long id) {
        StudentGroup studentGroup = studentGroupService.deleteStudentGroup(id);

        return mapper.toStudentGroupResponseDto(studentGroup);
    }
}
