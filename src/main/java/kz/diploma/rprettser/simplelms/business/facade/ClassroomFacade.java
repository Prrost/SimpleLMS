package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.ClassroomResponseDto;

import java.util.List;

public interface ClassroomFacade {
    List<ClassroomResponseDto> getAllClassrooms();

    ClassroomResponseDto getClassroomById(Long id);

    ClassroomResponseDto createClassroom(ClassroomRequestDto classroomRequestDto);

    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto classroomRequestDto);

    ClassroomResponseDto deleteClassroom(Long id);
}
