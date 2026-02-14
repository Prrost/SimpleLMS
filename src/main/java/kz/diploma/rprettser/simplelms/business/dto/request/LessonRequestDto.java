package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonRequestDto {

    private Long id;

    private String name;

    private Long classroomId;

    private Long studentGroupId;

    private LocalDateTime startsAt;

    private LocalDateTime endsAt;

    private LocalDateTime expiresAt;
}
