package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Optional<Lesson> getLessonById(Long id);

    List<Lesson> getAllLessons();

    Lesson createLesson(LessonRequestDto lessonDto);

    Lesson updateLesson(Long id, LessonRequestDto lessonDto);

    Lesson deleteLesson(Long id);
}
