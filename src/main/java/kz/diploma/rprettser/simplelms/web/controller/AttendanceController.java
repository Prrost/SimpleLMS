package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.AttendanceRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.AttendanceFacade;
import kz.diploma.rprettser.simplelms.business.service.LessonService;
import kz.diploma.rprettser.simplelms.business.service.StudentService;
import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import kz.diploma.rprettser.simplelms.dal.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static kz.diploma.rprettser.simplelms.common.utils.ObjectUtil.ifNullThrow;

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

    @PostMapping("/set_attendance")
    public AttendanceResponseDto setAttendance(
            @RequestBody AttendanceRequestDto request
    ){
        return attendanceFacade.setAttendanceForStudent(request);
    }

    @DeleteMapping("/{id}")
    public AttendanceResponseDto deleteAttendance(
            @PathVariable Long id
    ){
        return attendanceFacade.deleteAttendance(id);
    }
}
