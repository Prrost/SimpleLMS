package kz.diploma.rprettser.simplelms.business.facade;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttendanceFacade {
    List<AttendanceResponseDto> getAllAttendances();

    Page<AttendanceResponseDto> getAllAttendancesPageable(Pageable pageable);

    AttendanceResponseDto getAttendanceById(Long id);

    AttendanceResponseDto createAttendance(AttendanceRequestDto attendanceRequestDto);

    AttendanceResponseDto updateAttendance(Long id, AttendanceRequestDto attendanceRequestDto);

    AttendanceResponseDto deleteAttendance(Long id);

    AttendanceResponseDto setAttendanceForStudent(AttendanceRequestDto request);

    Page<AttendanceResponseDto> searchAttendances(Long studentId, Long lessonId, String attendanceMark, Pageable pageable);
}
