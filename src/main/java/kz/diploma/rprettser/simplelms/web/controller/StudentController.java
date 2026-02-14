package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.StudentRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.StudentResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.StudentFacade;
import lombok.RequiredArgsConstructor;
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
