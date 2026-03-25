package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Optional<Lesson> getLessonById(Long id);

    List<Lesson> getAllLessons();

    Page<Lesson> getAllLessonsPageable(Pageable pageable);

    Page<Lesson> searchLessons(String name, Long classroomId, Long studentGroupId, Pageable pageable);

    Lesson createLesson(LessonRequestDto lessonDto);

    Lesson updateLesson(Long id, LessonRequestDto lessonDto);

    Lesson deleteLesson(Long id);

    Optional<Lesson> findLessonByLessonName(String lessonName);
}
