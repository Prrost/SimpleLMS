package kz.diploma.rprettser.simplelms.business.service.impl;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.service.AttendanceService;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.common.constant.Constant;
import kz.diploma.rprettser.simplelms.dal.entity.Attendance;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import kz.diploma.rprettser.simplelms.dal.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.setIfNotNull;

@RequiredArgsConstructor
@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final StudentService studentService;
    private final LessonService lessonService;

    @Override
    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> getAllAttendances() {
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance createAttendance(AttendanceRequestDto attendanceDto) {
        Student student = null;
        Lesson lesson = null;

        if (attendanceDto.getStudentId() != null) {
            student = studentService.getStudentById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("No student found with id: " + attendanceDto.getStudentId()));
        }

        if (attendanceDto.getLessonId() != null) {
            lesson = lessonService.getLessonById(attendanceDto.getLessonId())
                    .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + attendanceDto.getLessonId()));
        }

        Attendance attendance = Attendance.builder()
                .student(student)
                .lesson(lesson)
                .attendanceMark(attendanceDto.getAttendanceMark())
                .createdBy(Constant.SYSTEM)
                .updatedBy(Constant.SYSTEM)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance updateAttendance(Long id, AttendanceRequestDto attendanceDto) {
        Attendance attendance = this.getAttendanceById(id)
                .orElseThrow(() -> new NoSuchElementException("No attendance found with id: " + id));

        if (attendanceDto.getStudentId() != null) {
            Student student = studentService.getStudentById(attendanceDto.getStudentId())
                    .orElseThrow(() -> new NoSuchElementException("No student found with id: " + attendanceDto.getStudentId()));
            attendance.setStudent(student);
        }

        if (attendanceDto.getLessonId() != null) {
            Lesson lesson = lessonService.getLessonById(attendanceDto.getLessonId())
                    .orElseThrow(() -> new NoSuchElementException("No lesson found with id: " + attendanceDto.getLessonId()));
            attendance.setLesson(lesson);
        }

        setIfNotNull(attendanceDto.getAttendanceMark(), attendance::setAttendanceMark);
        attendance.setUpdatedBy(Constant.SYSTEM);
        attendance.setUpdatedAt(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    @Override
    public Attendance deleteAttendance(Long id) {
        Attendance attendance = this.getAttendanceById(id)
                .orElseThrow(() -> new NoSuchElementException("No attendance found with id: " + id));

        attendance.setIsDeleted(true);
        attendance.setUpdatedBy(Constant.SYSTEM);
        attendance.setUpdatedAt(LocalDateTime.now());

        return attendanceRepository.save(attendance);
    }

    @Override
    public Optional<Attendance> findByStudentIdAndLessonId(Long studentId, Long lessonId) {
        return attendanceRepository.findFirstByStudentIdAndLessonId(studentId, lessonId);
    }
}
