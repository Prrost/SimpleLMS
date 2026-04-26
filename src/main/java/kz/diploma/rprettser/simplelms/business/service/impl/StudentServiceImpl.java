package kz.diploma.rprettser.simplelms.business.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import kz.diploma.rprettser.simplelms.dal.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Lazy
    @Autowired
    private StudentGroupService studentGroupService;

    @Override
    @Transactional
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Page<Student> getAllStudentsPageable(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Student> searchStudents(String name, String lastName, String email, String phone, Pageable pageable) {
        Specification<Student> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (lastName != null && !lastName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }
            if (email != null && !email.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + phone + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return studentRepository.findAll(spec, pageable);
    }

    @Override
    public List<Student> getAllStudentsByIds(List<Long> ids) {
        return studentRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public Student createStudent(StudentRequestDto studentDto) {
        Set<StudentGroup> studentGroups = new HashSet<>();

        if (studentDto.getStudentGroupsIds() != null) {

            var foundStudentGroups = studentGroupService.getAllStudentGroupsByIds(new ArrayList<>(studentDto.getStudentGroupsIds()));
            var foundStudentGroupsIds = foundStudentGroups.stream().map(StudentGroup::getId).toList();
            var notFoundStudentGroups = studentDto.getStudentGroupsIds().stream().filter(studentId -> !foundStudentGroupsIds.contains(studentId)).toList();

            if (!notFoundStudentGroups.isEmpty()) {
                throw new NoSuchElementException("No student groups found with id's: " + notFoundStudentGroups);
            }

            studentGroups.addAll(foundStudentGroups);
        }

        Student student = Student.builder()
                .name(studentDto.getName())
                .lastName(studentDto.getLastName())
                .email(studentDto.getEmail())
                .phone(studentDto.getPhone())
                .studentGroups(new HashSet<>())
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        studentGroups.forEach(student::addStudentGroup);

        return studentRepository.save(student);
    }

    @Override
    @Transactional
    public Student updateStudent(Long id, StudentRequestDto studentDto) {
        Student student = this.getStudentById(id).orElseThrow(() -> new NoSuchElementException("No student found with id: " + id));

        Set<StudentGroup> studentGroups = new HashSet<>();

        if (studentDto.getStudentGroupsIds() != null) {

            var foundStudentGroups = studentGroupService.getAllStudentGroupsByIds(new ArrayList<>(studentDto.getStudentGroupsIds()));
            var foundStudentGroupsIds = foundStudentGroups.stream().map(StudentGroup::getId).toList();
            var notFoundStudentGroups = studentDto.getStudentGroupsIds().stream().filter(studentId -> !foundStudentGroupsIds.contains(studentId)).toList();

            if (!notFoundStudentGroups.isEmpty()) {
                throw new NoSuchElementException("No student groups found with id's: " + notFoundStudentGroups);
            }

            studentGroups.addAll(foundStudentGroups);
        }

        student.removeAllStudentGroups();

        if (!studentGroups.isEmpty()){
            studentGroups.forEach(student::addStudentGroup);
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
    @Transactional
    public Student deleteStudent(Long id) {
        Student student = this.getStudentById(id).orElseThrow(() -> new NoSuchElementException("No student found with id: " + id));

        student.setIsDeleted(true);
        student.setUpdatedBy(Constant.SYSTEM);
        student.setUpdatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findStudentByStudentFullName(String studentName, String studentLastName) {
        return studentRepository.findByNameAndLastName(studentName, studentLastName);
    }
}
