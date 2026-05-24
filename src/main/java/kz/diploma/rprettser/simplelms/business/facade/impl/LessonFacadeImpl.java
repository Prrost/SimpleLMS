package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.LessonRepeatRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.LessonResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.LessonFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.AttendanceService;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class LessonFacadeImpl implements LessonFacade {

    private final LessonService lessonService;
    private final AttendanceService attendanceService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<LessonResponseDto> getAllLessons() {
        List<Lesson> lessons = lessonService.getAllLessons();

        return mapper.toListLessonResponseDto(lessons);
    }

    @Override
    @Transactional
    public Page<LessonResponseDto> getAllLessonsPageable(Pageable pageable) {
        Page<Lesson> lessons = lessonService.getAllLessonsPageable(pageable);

        return lessons.map(mapper::toLessonResponseDto);
    }

    @Override
    @Transactional
    public Page<LessonResponseDto> searchLessons(String name, Long classroomId, Long studentGroupId, Pageable pageable) {
        return mapper.toPageableLessonResponseDto(
                lessonService.searchLessons(name, classroomId, studentGroupId, pageable)
        );
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
        attendanceService.initAttendancesForLesson(lesson);

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

    @Override
    @Transactional
    public List<LessonResponseDto> repeatLesson(Long id, LessonRepeatRequestDto dto) {
        List<Lesson> lessons = lessonService.repeatLesson(id, dto.getWeeks());
        lessons.forEach(attendanceService::initAttendancesForLesson);

        return mapper.toListLessonResponseDto(lessons);
    }
}
