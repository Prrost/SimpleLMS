package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;

import java.util.List;

public interface AttendanceFacade {
    List<AttendanceResponseDto> getAllAttendances();

    AttendanceResponseDto getAttendanceById(Long id);

    AttendanceResponseDto createAttendance(AttendanceRequestDto attendanceRequestDto);

    AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto attendanceRequestDto);

    AttendanceResponseDto deleteAttendance(Long id);
}
