package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Classroom;

import java.util.List;
import java.util.Optional;

public interface ClassroomService {
    Optional<Classroom> getClassroomById(Long id);

    List<Classroom> getAllClassrooms();

    Classroom createClassroom(ClassroomRequestDto classroomDto);

    Classroom updateClassroom(Long id, ClassroomRequestDto classroomDto);

    Classroom deleteClassroom(Long id);
}
