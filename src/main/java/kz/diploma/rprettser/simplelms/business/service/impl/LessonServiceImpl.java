package kz.diploma.rprettser.simplelms.business.service.impl;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class LessonServiceImpl implements LessonService {

    private static final DateTimeFormatter DATE_SUFFIX = DateTimeFormatter.ofPattern("dd.MM");

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
    public Page<Lesson> getAllLessonsPageable(Pageable pageable) {
        return lessonRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Page<Lesson> searchLessons(String name, Long classroomId, Long studentGroupId, Pageable pageable) {
        Specification<Lesson> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (classroomId != null) {
                predicates.add(criteriaBuilder.equal(root.get("classroom").get("id"), classroomId));
            }
            if (studentGroupId != null) {
                predicates.add(criteriaBuilder.equal(root.get("studentGroup").get("id"), studentGroupId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return lessonRepository.findAll(spec, pageable);
    }

    @Override
    public Lesson createLesson(LessonRequestDto lessonDto) {
        if (lessonDto.getStartsAt() != null && lessonDto.getEndsAt() != null
                && !lessonDto.getStartsAt().isBefore(lessonDto.getEndsAt())) {
            throw new IllegalArgumentException("Lesson start time must be before end time");
        }

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

        String finalName = buildLessonName(lessonDto.getName(), studentGroup, lessonDto.getStartsAt());

        lessonRepository.findByNameAndIsDeletedFalse(finalName).ifPresent(l -> {
            throw new IllegalArgumentException("Lesson with name '" + finalName + "' already exists");
        });

        Lesson lesson = Lesson.builder()
                .name(finalName)
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

        LocalDateTime effectiveStart = lessonDto.getStartsAt() != null ? lessonDto.getStartsAt() : lesson.getStartsAt();
        LocalDateTime effectiveEnd = lessonDto.getEndsAt() != null ? lessonDto.getEndsAt() : lesson.getEndsAt();
        if (effectiveStart != null && effectiveEnd != null && !effectiveStart.isBefore(effectiveEnd)) {
            throw new IllegalArgumentException("Lesson start time must be before end time");
        }

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

        if (lessonDto.getName() != null) {
            StudentGroup effectiveGroup = lesson.getStudentGroup();
            LocalDateTime effectiveStartForName = effectiveStart != null ? effectiveStart : lesson.getStartsAt();
            String finalName = buildLessonName(lessonDto.getName(), effectiveGroup, effectiveStartForName);

            lessonRepository.findByNameAndIsDeletedFalse(finalName)
                    .filter(l -> !l.getId().equals(id))
                    .ifPresent(l -> {
                        throw new IllegalArgumentException("Lesson with name '" + finalName + "' already exists");
                    });

            lesson.setName(finalName);
        }

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

    @Override
    public Optional<Lesson> findLessonByLessonName(String lessonName) {
        return lessonRepository.findByName(lessonName);
    }

    @Override
    @Transactional
    public List<Lesson> repeatLesson(Long id, int weeks) {
        Lesson source = this.getLessonById(id)
                .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + id));

        String baseName = extractBaseName(source.getName());
        StudentGroup group = source.getStudentGroup();
        List<Lesson> created = new ArrayList<>();

        for (int i = 1; i <= weeks; i++) {
            LocalDateTime newStart   = source.getStartsAt() != null ? source.getStartsAt().plusWeeks(i) : null;
            LocalDateTime newEnd     = source.getEndsAt()   != null ? source.getEndsAt().plusWeeks(i)   : null;
            LocalDateTime newExpires = source.getExpiresAt() != null ? source.getExpiresAt().plusWeeks(i) : null;

            String finalName = buildLessonName(baseName, group, newStart);

            if (lessonRepository.findByNameAndIsDeletedFalse(finalName).isPresent()) {
                continue;
            }

            Lesson copy = Lesson.builder()
                    .name(finalName)
                    .classroom(source.getClassroom())
                    .studentGroup(group)
                    .startsAt(newStart)
                    .endsAt(newEnd)
                    .expiresAt(newExpires)
                    .createdBy(Constant.SYSTEM)
                    .updatedBy(Constant.SYSTEM)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isDeleted(false)
                    .build();

            created.add(lessonRepository.save(copy));
        }

        return created;
    }

    private String buildLessonName(String baseName, StudentGroup group, LocalDateTime startsAt) {
        String groupName = group != null ? group.getName() : "—";
        String dateStr   = startsAt != null ? startsAt.format(DATE_SUFFIX) : "??";
        return baseName + " – " + groupName + " – " + dateStr;
    }

    private String extractBaseName(String storedName) {
        if (storedName == null) return "";
        // Format is: "{baseName} – {groupName} – {dd.MM}"
        // Split from the right: last two " – " segments are groupName and date
        int lastSep = storedName.lastIndexOf(" – ");
        if (lastSep < 0) return storedName;
        String withoutDate = storedName.substring(0, lastSep);
        int prevSep = withoutDate.lastIndexOf(" – ");
        if (prevSep < 0) return storedName;
        return withoutDate.substring(0, prevSep);
    }
}
