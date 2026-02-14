package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import kz.diploma.rprettser.simplelms.dal.repository.StudentGroupRepository;
import kz.diploma.rprettser.simplelms.dal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class StudentGroupServiceImpl implements StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;
    private final StudentRepository studentRepository;


    @Override
    public Optional<StudentGroup> getStudentGroupById(Long id) {
        return studentGroupRepository.findById(id);
    }

    @Override
    public List<StudentGroup> getAllStudentGroups() {
        return studentGroupRepository.findAll();
    }

    @Override
    public StudentGroup createStudentGroup(StudentGroupRequestDto studentGroupDto) {
        Set<Student> students = new HashSet<>();

        if (studentGroupDto.getStudentIds() != null) {
            studentGroupDto.getStudentIds()
                    .forEach(studentId -> students.add(studentRepository.findById(studentId)
                            .orElseThrow(() -> new NoSuchElementException("No student found with id: " + studentId))));
        }

        StudentGroup studentGroup = StudentGroup.builder()
                .name(studentGroupDto.getName())
                .students(students)
                .isVirtual(studentGroupDto.getIsVirtual())
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return studentGroupRepository.save(studentGroup);
    }

    @Override
    public StudentGroup updateStudentGroup(Long id, StudentGroupRequestDto studentGroupDto) {
        StudentGroup studentGroup = this.getStudentGroupById(id)
                .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + id));

        Set<Student> students = new HashSet<>();

        if (studentGroupDto.getStudentIds() != null) {
            studentGroupDto.getStudentIds()
                    .forEach(studentId -> students.add(studentRepository.findById(studentId)
                            .orElseThrow(() -> new NoSuchElementException("No student found with id: " + studentId))));
        }

        if (!students.isEmpty()) {
            studentGroup.setStudents(students);
        }

        setIfNotNull(studentGroupDto.getName(), studentGroup::setName);
        setIfNotNull(studentGroupDto.getIsVirtual(), studentGroup::setIsVirtual);
        studentGroup.setUpdatedBy(Constant.SYSTEM);
        studentGroup.setUpdatedAt(LocalDateTime.now());

        return studentGroupRepository.save(studentGroup);
    }

    @Override
    public StudentGroup deleteStudentGroup(Long id) {
        StudentGroup studentGroup = this.getStudentGroupById(id)
                .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + id));

        studentGroup.setIsDeleted(true);
        studentGroup.setUpdatedBy(Constant.SYSTEM);
        studentGroup.setUpdatedAt(LocalDateTime.now());

        return studentGroupRepository.save(studentGroup);
    }
}
