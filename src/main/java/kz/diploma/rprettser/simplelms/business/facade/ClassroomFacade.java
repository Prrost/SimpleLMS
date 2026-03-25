package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.ClassroomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassroomFacade {
    List<ClassroomResponseDto> getAllClassrooms();

    Page<ClassroomResponseDto> searchClassrooms(String name, Pageable pageable);

    Page<ClassroomResponseDto> getAllClassroomsPageable(Pageable pageable);

    ClassroomResponseDto getClassroomById(Long id);

    ClassroomResponseDto createClassroom(ClassroomRequestDto classroomRequestDto);

    ClassroomResponseDto updateClassroom(Long id, ClassroomRequestDto classroomRequestDto);

    ClassroomResponseDto deleteClassroom(Long id);
}
