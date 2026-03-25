package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Optional<Attendance> getAttendanceById(Long id);

    List<Attendance> getAllAttendances();

    Page<Attendance> getAllAttendancesPageable(Pageable pageable);

    Page<Attendance> searchAttendances(Long studentId, Long lessonId, String attendanceMark, Pageable pageable);

    Attendance createAttendance(AttendanceRequestDto attendanceDto);

    Attendance updateAttendance(Long id, AttendanceRequestDto attendanceDto);

    Attendance deleteAttendance(Long id);

    Optional<Attendance> findByStudentIdAndLessonId(Long studentId, Long lessonId);
}
