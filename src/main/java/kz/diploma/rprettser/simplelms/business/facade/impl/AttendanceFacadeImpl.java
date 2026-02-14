package kz.diploma.rprettser.simplelms.business.facade.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.AttendanceFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.AttendanceService;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.dal.entity.Attendance;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.ifNullThrow;

@RequiredArgsConstructor
@Component
public class AttendanceFacadeImpl implements AttendanceFacade {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final LessonService lessonService;
    private final Mapper mapper;

    @Override
    @Transactional
    public List<AttendanceResponseDto> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendances();

        return mapper.toListAttendanceResponseDto(attendances);
    }

    @Override
    @Transactional
    public AttendanceResponseDto getAttendanceById(Long id) {
        Attendance attendance = attendanceService.getAttendanceById(id)
                .orElseThrow(() -> new NoSuchElementException("No attendance found with id: " + id));

        return mapper.toAttendanceResponseDto(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponseDto createAttendance(AttendanceRequestDto attendanceRequestDto) {
        Attendance attendance = attendanceService.createAttendance(attendanceRequestDto);

        return mapper.toAttendanceResponseDto(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto attendanceRequestDto) {
        Attendance attendance = attendanceService.updateAttendance(id, attendanceRequestDto);

        return mapper.toAttendanceResponseDto(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponseDto deleteAttendance(Long id) {
        Attendance attendance = attendanceService.deleteAttendance(id);

        return mapper.toAttendanceResponseDto(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponseDto setAttendanceForStudent(AttendanceRequestDto request) {
        ifNullThrow(request.getStudentName(), "Student name is null");
        ifNullThrow(request.getLessonName(), "Lesson name is null");
        ifNullThrow(request.getAttendanceMark(), "Attendance mark is null");
        Student student = studentService.findStudentByStudentName(request.getStudentName()).orElseThrow(() -> new NoSuchElementException("Student not found with name: " + request.getStudentName()));
        Lesson lesson = lessonService.findLessonByLessonName(request.getLessonName()).orElseThrow(() -> new NoSuchElementException("Lesson not found with name: " + request.getLessonName()));

        Attendance att = attendanceService.findByStudentIdAndLessonId(student.getId(), lesson.getId()).orElseThrow(() -> new NoSuchElementException("Attendance not found for student: " + student.getName() + " and lesson: " + lesson.getName()));
        Attendance changedAtt =  attendanceService.updateAttendance(
                att.getId(),
                AttendanceRequestDto.builder().attendanceMark(request.getAttendanceMark()).build()
        );

        return mapper.toAttendanceResponseDto(changedAtt);
    }
}
