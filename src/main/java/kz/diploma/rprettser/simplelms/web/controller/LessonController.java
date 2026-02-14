package kz.diploma.rprettser.simplelms.web.controller;

import kz.diploma.rprettser.simplelms.business.dto.request.LessonRequestDto;
import kz.diploma.rprettser.simplelms.business.dto.response.LessonResponseDto;
import kz.diploma.rprettser.simplelms.business.facade.LessonFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    private final LessonFacade lessonFacade;

    @GetMapping("/all")
    public List<LessonResponseDto> getAllLessons(){
        return lessonFacade.getAllLessons();
    }

    @GetMapping("/{id}")
    public LessonResponseDto getLessonById(
            @PathVariable Long id
    ){
        return lessonFacade.getLessonById(id);
    }

    @PostMapping()
    public LessonResponseDto createLesson(
            @RequestBody LessonRequestDto lessonRequestDto
    ){
        return lessonFacade.createLesson(lessonRequestDto);
    }

    @PutMapping("/{id}")
    public LessonResponseDto updateLesson(
            @PathVariable Long id,
            @RequestBody LessonRequestDto lessonRequestDto
    ){
        return lessonFacade.updateLesson(id, lessonRequestDto);
    }

    @DeleteMapping("/{id}")
    public LessonResponseDto deleteLesson(
            @PathVariable Long id
    ){
        return lessonFacade.deleteLesson(id);
    }
}
