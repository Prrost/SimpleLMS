package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import kz.diploma.rprettser.simplelms.dal.enums.AttendanceMark;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceRequestDto {

    private Long id;

    @NotNull
    private Long studentId;

    @NotNull
    private Long lessonId;

    private String studentName;

    private String lessonName;

    @NotNull
    private AttendanceMark attendanceMark;
}
