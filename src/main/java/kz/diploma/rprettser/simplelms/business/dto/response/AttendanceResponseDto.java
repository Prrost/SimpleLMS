package kz.diploma.rprettser.simplelms.business.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.diploma.rprettser.simplelms.dal.enums.AttendanceMark;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceResponseDto {

    private Long id;

    private StudentShortDto student;

    private LessonResponseDto lesson;

    private AttendanceMark attendanceMark;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
