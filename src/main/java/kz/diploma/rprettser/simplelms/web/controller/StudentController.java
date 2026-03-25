package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/student")
public class StudentController {

    private final StudentFacade studentFacade;

    @GetMapping("/all")
    public List<StudentResponseDto> getAllStudents(){
        return studentFacade.getAllStudents();
    }

    @GetMapping
    public Page<StudentResponseDto> getAllStudentsPageable(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return studentFacade.searchStudents(name, lastName, email, phone, pageable);
    }

    @GetMapping("/{id}")
    public StudentResponseDto getStudentById(
            @PathVariable Long id
    ){
        return studentFacade.getStudentById(id);
    }

    @PostMapping()
    public StudentResponseDto createStudent(
            @RequestBody StudentRequestDto studentRequestDto
    ){
        return studentFacade.createStudent(studentRequestDto);
    }

    @PutMapping("/{id}")
    public StudentResponseDto updateStudent(
            @PathVariable Long id,
            @RequestBody StudentRequestDto studentRequestDto
    ){
        return studentFacade.updateStudent(id, studentRequestDto);
    }

    @DeleteMapping("/{id}")
    public StudentResponseDto deleteStudent(
            @PathVariable Long id
    ){
        return studentFacade.deleteStudent(id);
    }


}
