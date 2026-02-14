package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.business.service.ClassroomService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Classroom;
import kz.diploma.rprettser.simplelms.dal.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepository;

    @Override
    public Optional<Classroom> getClassroomById(Long id) {
        return classroomRepository.findById(id);
    }

    @Override
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    @Override
    public Classroom createClassroom(ClassroomRequestDto classroomDto) {
        Classroom classroom = Classroom.builder()
                .name(classroomDto.getName())
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom updateClassroom(Long id, ClassroomRequestDto classroomDto) {
        Classroom classroom = this.getClassroomById(id)
                .orElseThrow(() -> new NoSuchElementException("No classroom found with id: " + id));

        setIfNotNull(classroomDto.getName(), classroom::setName);
        classroom.setUpdatedBy(Constant.SYSTEM);
        classroom.setUpdatedAt(LocalDateTime.now());

        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom deleteClassroom(Long id) {
        Classroom classroom = this.getClassroomById(id)
                .orElseThrow(() -> new NoSuchElementException("No classroom found with id: " + id));

        classroom.setIsDeleted(true);
        classroom.setUpdatedBy(Constant.SYSTEM);
        classroom.setUpdatedAt(LocalDateTime.now());

        return classroomRepository.save(classroom);
    }
}
