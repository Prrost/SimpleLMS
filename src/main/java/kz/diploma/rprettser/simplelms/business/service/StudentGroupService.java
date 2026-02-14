package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;

import java.util.List;
import java.util.Optional;

public interface StudentGroupService {

    Optional<StudentGroup> getStudentGroupById(Long id);

    List<StudentGroup> getAllStudentGroups();

    StudentGroup createStudentGroup(StudentGroupRequestDto studentGroupDto);

    StudentGroup updateStudentGroup(Long id, StudentGroupRequestDto studentGroupDto);

    StudentGroup deleteStudentGroup(Long id);
}
