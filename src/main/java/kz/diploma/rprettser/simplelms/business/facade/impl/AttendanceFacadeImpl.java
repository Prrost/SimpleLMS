package kz.diploma.rprettser.simplelms.business.facade.impl;

import jakarta.transaction.Transactional;
import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.AttendanceFacade;
import kz.diploma.rprettser.simplelms.business.mapper.Mapper;
import kz.diploma.rprettser.simplelms.business.service.AttendanceService;
import kz.diploma.rprettser.simplelms.dal.entity.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class AttendanceFacadeImpl implements AttendanceFacade {

    private final AttendanceService attendanceService;
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
}
