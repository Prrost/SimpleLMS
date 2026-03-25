package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentGroupService {

    Optional<StudentGroup> getStudentGroupById(Long id);

    List<StudentGroup> getAllStudentGroups();

    Page<StudentGroup> getAllStudentGroupsPageable(Pageable pageable);

    Page<StudentGroup> searchStudentGroups(String name, Boolean isVirtual, Pageable pageable);

    List<StudentGroup> getAllStudentGroupsByIds(List<Long> ids);

    StudentGroup createStudentGroup(StudentGroupRequestDto studentGroupDto);

    StudentGroup updateStudentGroup(Long id, StudentGroupRequestDto studentGroupDto);

    StudentGroup deleteStudentGroup(Long id);
}
