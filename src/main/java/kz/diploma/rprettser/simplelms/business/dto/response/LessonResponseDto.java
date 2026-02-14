package kz.diploma.rprettser.simplelms.business.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonResponseDto {

    private Long id;

    private String name;

    private ClassroomResponseDto classroom;

    private StudentGroupShortDto studentGroup;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
