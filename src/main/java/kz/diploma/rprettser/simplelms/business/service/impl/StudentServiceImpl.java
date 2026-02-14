package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import kz.diploma.rprettser.simplelms.dal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentGroupService studentGroupService;

    @Override
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student createStudent(StudentRequestDto studentDto) {
        Set<StudentGroup> studentGroups = new HashSet<>();

        if (studentDto.getStudentGroupsIds() != null) {
            studentDto.getStudentGroupsIds().forEach(
                    sgId -> studentGroups.add(studentGroupService.getStudentGroupById(sgId)
                            .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + sgId)))
            );
        }

        Student student = Student.builder()
                .name(studentDto.getName())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .phone(studentDto.getPhone())
                .studentGroups(studentGroups)
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Long id, StudentRequestDto studentDto) {
        Student student = this.getStudentById(id).orElseThrow(() -> new NoSuchElementException("No student found with id: " + id));

        Set<StudentGroup> studentGroups = new HashSet<>();

        if (studentDto.getStudentGroupsIds() != null) {
            studentDto.getStudentGroupsIds().forEach(
                    sgId -> studentGroups.add(studentGroupService.getStudentGroupById(sgId)
                            .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + sgId)))
            );
        }

        if (!studentGroups.isEmpty()){
            student.setStudentGroups(studentGroups);
        }

        setIfNotNull(studentDto.getName(), student::setName);
        setIfNotNull(studentDto.getLastName(), student::setLastName);
        setIfNotNull(studentDto.getEmail(), student::setEmail);
        setIfNotNull(studentDto.getPhone(), student::setPhone);
        student.setUpdatedBy(Constant.SYSTEM);
        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    @Override
    public Student deleteStudent(Long id) {
        Student student = this.getStudentById(id).orElseThrow(() -> new NoSuchElementException("No student found with id: " + id));

        student.setIsDeleted(true);
        student.setUpdatedBy(Constant.SYSTEM);
        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }
}
