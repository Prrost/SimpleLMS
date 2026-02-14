package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.AttendanceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceFacade attendanceFacade;

    @GetMapping("/all")
    public List<AttendanceResponseDto> getAllAttendances(){
        return attendanceFacade.getAllAttendances();
    }

    @GetMapping("/{id}")
    public AttendanceResponseDto getAttendanceById(
            @PathVariable Long id
    ){
        return attendanceFacade.getAttendanceById(id);
    }

    @PostMapping()
    public AttendanceResponseDto createAttendance(
            @RequestBody AttendanceRequestDto attendanceRequestDto
    ){
        return attendanceFacade.createAttendance(attendanceRequestDto);
    }

    @PutMapping("/{id}")
    public AttendanceResponseDto updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceRequestDto attendanceRequestDto
    ){
        return attendanceFacade.updateAttendance(id, attendanceRequestDto);
    }

    @DeleteMapping("/{id}")
    public AttendanceResponseDto deleteAttendance(
            @PathVariable Long id
    ){
        return attendanceFacade.deleteAttendance(id);
    }
}
