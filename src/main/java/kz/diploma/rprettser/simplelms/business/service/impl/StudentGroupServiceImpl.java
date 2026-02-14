package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import kz.diploma.rprettser.simplelms.dal.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class StudentGroupServiceImpl implements StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;

    public StudentGroup findStudentGroupId(Long id){
        return studentGroupRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No student group found with id: " + id));
    }
}
