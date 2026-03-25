package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentGroupRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentGroupResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentGroupFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping
    public Page<StudentGroupResponseDto> getAllStudentGroupPageable(
            @RequestParam(required = false) Boolean isVirtual,
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return studentGroupFacade.searchStudentGroups(name, isVirtual, pageable);
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
