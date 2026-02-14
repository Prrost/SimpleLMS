package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentGroupFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/student_group")
public class StudentGroupController {

    private final StudentGroupFacade studentGroupFacade;

    @GetMapping("/all")
    public List<StudentGroupResponseDto> getAllStudentGroups(){
        return studentGroupFacade.getAllStudentGroups();
    }

    @GetMapping("/{id}")
    public StudentGroupResponseDto getStudentGroupById(
            @PathVariable Long id
    ){
        return studentGroupFacade.getStudentGroupById(id);
    }

    @PostMapping()
    public StudentGroupResponseDto createStudentGroup(
            @RequestBody StudentGroupRequestDto studentGroupRequestDto
    ){
        return studentGroupFacade.createStudentGroup(studentGroupRequestDto);
    }

    @PutMapping("/{id}")
    public StudentGroupResponseDto updateStudentGroup(
            @PathVariable Long id,
            @RequestBody StudentGroupRequestDto studentGroupRequestDto
    ){
        return studentGroupFacade.updateStudentGroup(id, studentGroupRequestDto);
    }

    @DeleteMapping("/{id}")
    public StudentGroupResponseDto deleteStudentGroup(
            @PathVariable Long id
    ){
        return studentGroupFacade.deleteStudentGroup(id);
    }
}
