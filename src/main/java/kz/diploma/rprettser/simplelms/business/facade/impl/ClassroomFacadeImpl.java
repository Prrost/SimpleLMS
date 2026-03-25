package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.dto.response.ClassroomResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.ClassroomFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.ClassroomService;
import kz.diploma.rprettser.simplelms.dal.entity.Classroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class ClassroomFacadeImpl implements ClassroomFacade {

    private final ClassroomService classroomService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<ClassroomResponseDto> getAllClassrooms() {
        List<Classroom> classrooms = classroomService.getAllClassrooms();

        return mapper.toListClassroomResponseDto(classrooms);
    }

    @Override
    @Transactional
    public Page<ClassroomResponseDto> searchClassrooms(String name, Pageable pageable) {
        return mapper.toPageableClassroomResponseDto(
                classroomService.searchClassrooms(name, pageable)
        );
    }

    @Override
    @Transactional
    public Page<ClassroomResponseDto> getAllClassroomsPageable(Pageable pageable) {
        Page<Classroom> classrooms = classroomService.getAllClassroomsPageable(pageable);

        return classrooms.map(mapper::toClassroomResponseDto);
    }

    @Override
    @Transactional
    public ClassroomResponseDto getClassroomById(Long id) {
        Classroom classroom = classroomService.getClassroomById(id)
                .orElseThrow(() -> new NoSuchElementException("No classroom found with id: " + id));

        return mapper.toClassroomResponseDto(classroom);
    }

    @Override
    @Transactional
    public ClassroomResponseDto createClassroom(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = classroomService.createClassroom(classroomRequestDto);

        return mapper.toClassroomResponseDto(classroom);
    }

    @Override
    @Transactional
    public ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = classroomService.updateClassroom(id, classroomRequestDto);

        return mapper.toClassroomResponseDto(classroom);
    }

    @Override
    @Transactional
    public ClassroomResponseDto deleteClassroom(Long id) {
        Classroom classroom = classroomService.deleteClassroom(id);

        return mapper.toClassroomResponseDto(classroom);
    }
}
