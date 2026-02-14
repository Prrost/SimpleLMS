package kz.diploma.rprettser.simplelms.business.mapper;


import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupShortDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentShortDto;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Mapper {

    public StudentResponseDto toStudentResponseDto(Student student){
        return StudentResponseDto.builder().
                id(student.getId()).
                name(student.getName()).
                lastName(student.getLastName()).
                email(student.getEmail()).
                phone(student.getPhone()).
                createdAt(student.getCreatedAt()).
                updatedAt(student.getUpdatedAt()).
                createdBy(student.getCreatedBy()).
                updatedBy(student.getUpdatedBy()).
                isDeleted(student.getIsDeleted()).
                studentGroups(
                        toSetStudentGroupShortDto(student.getStudentGroups())
                ).build();
    }

    public List<StudentResponseDto> toListStudentResponseDto(List<Student> students){
        return students.stream().map(this::toStudentResponseDto).toList();
    }

    public StudentShortDto toStudentShortDto(Student student){
        return StudentShortDto.builder()
                .id(student.getId())
                .name(student.getName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .createdBy(student.getCreatedBy())
                .updatedBy(student.getUpdatedBy())
                .isDeleted(student.getIsDeleted())
                .build();
    }

    public Set<StudentShortDto> toSetStudentShortDto(Collection<Student> students){
        return students.stream().map(this::toStudentShortDto).collect(Collectors.toSet());
    }

    public StudentGroupShortDto toStudentGroupShortDto(StudentGroup studentGroup){
        return StudentGroupShortDto.builder().
                id(studentGroup.getId()).
                name(studentGroup.getName()).
                isVirtual(studentGroup.getIsVirtual()).
                createdAt(studentGroup.getCreatedAt()).
                updatedAt(studentGroup.getUpdatedAt()).
                createdBy(studentGroup.getCreatedBy()).
                updatedBy(studentGroup.getUpdatedBy()).
                isDeleted(studentGroup.getIsDeleted()).
                build();
    }

    public Set<StudentGroupShortDto> toSetStudentGroupShortDto(Collection<StudentGroup> studentGroups){
        return studentGroups.stream().map(this::toStudentGroupShortDto).collect(Collectors.toSet());
    }

    public StudentGroupResponseDto toStudentGroupResponseDto(StudentGroup studentGroup){
        return StudentGroupResponseDto.builder()
                .id(studentGroup.getId())
                .name(studentGroup.getName())
                .students(toSetStudentShortDto(studentGroup.getStudents()))
                .isVirtual(studentGroup.getIsVirtual())
                .createdAt(studentGroup.getCreatedAt())
                .updatedAt(studentGroup.getUpdatedAt())
                .createdBy(studentGroup.getCreatedBy())
                .updatedBy(studentGroup.getUpdatedBy())
                .isDeleted(studentGroup.getIsDeleted())
                .build();
    }

    public List<StudentGroupResponseDto> toListStudentGroupResponseDto(List<StudentGroup> studentGroups){
        return studentGroups.stream().map(this::toStudentGroupResponseDto).toList();
    }

}
