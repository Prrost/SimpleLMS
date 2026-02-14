package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.LessonResponseDto;

import java.util.List;

public interface LessonFacade {
    List<LessonResponseDto> getAllLessons();

    LessonResponseDto getLessonById(Long id);

    LessonResponseDto createLesson(LessonRequestDto lessonRequestDto);

    LessonResponseDto updateLesson(Long id, LessonRequestDto lessonRequestDto);

    LessonResponseDto deleteLesson(Long id);
}
