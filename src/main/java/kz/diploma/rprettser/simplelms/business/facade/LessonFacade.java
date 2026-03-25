package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.LessonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonFacade {
    List<LessonResponseDto> getAllLessons();

    Page<LessonResponseDto> getAllLessonsPageable(Pageable pageable);

    Page<LessonResponseDto> searchLessons(String name, Long classroomId, Long studentGroupId, Pageable pageable);

    LessonResponseDto getLessonById(Long id);

    LessonResponseDto createLesson(LessonRequestDto lessonRequestDto);

    LessonResponseDto updateLesson(Long id, LessonRequestDto lessonRequestDto);

    LessonResponseDto deleteLesson(Long id);
}
