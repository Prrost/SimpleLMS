package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.LessonResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.LessonFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class LessonFacadeImpl implements LessonFacade {

    private final LessonService lessonService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<LessonResponseDto> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();

        return mapper.toListLessonResponseDto(lessons);
    }

    @Override
    @Transactional
    public LessonResponseDto getLessonById(Long id) {
        Lesson lesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + id));

        return mapper.toLessonResponseDto(lesson);
    }

    @Override
    @Transactional
    public LessonResponseDto createLesson(LessonRequestDto lessonRequestDto) {
        Lesson lesson = lessonService.createLesson(lessonRequestDto);

        return mapper.toLessonResponseDto(lesson);
    }

    @Override
    @Transactional
    public LessonResponseDto updateLesson(Long id, LessonRequestDto lessonRequestDto) {
        Lesson lesson = lessonService.updateLesson(id, lessonRequestDto);

        return mapper.toLessonResponseDto(lesson);
    }

    @Override
    @Transactional
    public LessonResponseDto deleteLesson(Long id) {
        Lesson lesson = lessonService.deleteLesson(id);

        return mapper.toLessonResponseDto(lesson);
    }
}
