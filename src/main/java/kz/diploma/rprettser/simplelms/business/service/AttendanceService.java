package kz.diploma.rprettser.simplelms.business.service;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.dal.entity.Attendance;

import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Optional<Attendance> getAttendanceById(Long id);

    List<Attendance> getAllAttendances();

    Attendance createAttendance(AttendanceRequestDto attendanceDto);

    Attendance updateAttendance(Long id, AttendanceRequestDto attendanceDto);

    Attendance deleteAttendance(Long id);
}
