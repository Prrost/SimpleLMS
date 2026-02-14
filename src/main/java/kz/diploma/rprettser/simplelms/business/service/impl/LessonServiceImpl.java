package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.service.ClassroomService;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.business.service.StudentGroupService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Classroom;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import kz.diploma.rprettser.simplelms.dal.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ClassroomService classroomService;
    private final StudentGroupService studentGroupService;

    @Override
    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public Lesson createLesson(LessonRequestDto lessonDto) {
        Classroom classroom = null;
        StudentGroup studentGroup = null;

        if (lessonDto.getClassroomId() != null) {
            classroom = classroomService.getClassroomById(lessonDto.getClassroomId())
                    .orElseThrow(() -> new NoSuchElementException("No classroom found with id: " + lessonDto.getClassroomId()));
        }

        if (lessonDto.getStudentGroupId() != null) {
            studentGroup = studentGroupService.getStudentGroupById(lessonDto.getStudentGroupId())
                    .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + lessonDto.getStudentGroupId()));
        }

        Lesson lesson = Lesson.builder()
                .name(lessonDto.getName())
                .classroom(classroom)
                .studentGroup(studentGroup)
                .startsAt(lessonDto.getStartsAt())
                .endsAt(lessonDto.getEndsAt())
                .expiresAt(lessonDto.getExpiresAt())
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson updateLesson(Long id, LessonRequestDto lessonDto) {
        Lesson lesson = this.getLessonById(id)
                .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + id));

        if (lessonDto.getClassroomId() != null) {
            Classroom classroom = classroomService.getClassroomById(lessonDto.getClassroomId())
                    .orElseThrow(() -> new NoSuchElementException("No classroom found with id: " + lessonDto.getClassroomId()));
            lesson.setClassroom(classroom);
        }

        if (lessonDto.getStudentGroupId() != null) {
            StudentGroup studentGroup = studentGroupService.getStudentGroupById(lessonDto.getStudentGroupId())
                    .orElseThrow(() -> new NoSuchElementException("No student group found with id: " + lessonDto.getStudentGroupId()));
            lesson.setStudentGroup(studentGroup);
        }

        setIfNotNull(lessonDto.getName(), lesson::setName);
        setIfNotNull(lessonDto.getStartsAt(), lesson::setStartsAt);
        setIfNotNull(lessonDto.getEndsAt(), lesson::setEndsAt);
        setIfNotNull(lessonDto.getExpiresAt(), lesson::setExpiresAt);
        lesson.setUpdatedBy(Constant.SYSTEM);
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson deleteLesson(Long id) {
        Lesson lesson = this.getLessonById(id)
                .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + id));

        lesson.setIsDeleted(true);
        lesson.setUpdatedBy(Constant.SYSTEM);
        lesson.setUpdatedAt(LocalDateTime.now());

        return lessonRepository.save(lesson);
    }
}
