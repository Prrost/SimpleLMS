package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Classroom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    Optional<Classroom> getClassroomById(Long id);

    List<Classroom> getAllClassrooms();

    Page<Classroom> getAllClassroomsPageable(Pageable pageable);

    Page<Classroom> searchClassrooms(String name, Pageable pageable);

    Classroom createClassroom(ClassroomRequestDto classroomDto);

    Classroom updateClassroom(Long id, ClassroomRequestDto classroomDto);

    Classroom deleteClassroom(Long id);
}
