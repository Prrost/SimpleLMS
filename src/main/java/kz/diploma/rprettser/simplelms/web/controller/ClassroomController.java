package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.ClassroomRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.AttendanceResponseDto;
import kz.diploma.rprettser.simplelms.business.dto.response.ClassroomResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.ClassroomFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/classroom")
public class ClassroomController {

    private final ClassroomFacade classroomFacade;

    @GetMapping("/all")
    public List<ClassroomResponseDto> getAllClassrooms(){
        return classroomFacade.getAllClassrooms();
    }

    @GetMapping
    public Page<ClassroomResponseDto> getAllClassroomsPageable(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return classroomFacade.searchClassrooms(name, pageable);
    }

    @GetMapping("/{id}")
    public ClassroomResponseDto getClassroomById(
            @PathVariable Long id
    ){
        return classroomFacade.getClassroomById(id);
    }

    @PostMapping()
    public ClassroomResponseDto createClassroom(
            @RequestBody ClassroomRequestDto classroomRequestDto
    ){
        return classroomFacade.createClassroom(classroomRequestDto);
    }

    @PutMapping("/{id}")
    public ClassroomResponseDto updateClassroom(
            @PathVariable Long id,
            @RequestBody ClassroomRequestDto classroomRequestDto
    ){
        return classroomFacade.updateClassroom(id, classroomRequestDto);
    }

    @DeleteMapping("/{id}")
    public ClassroomResponseDto deleteClassroom(
            @PathVariable Long id
    ){
        return classroomFacade.deleteClassroom(id);
    }
}
