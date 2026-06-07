package kz.diploma.rprettser.simplelms.business.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kz.diploma.rprettser.simplelms.dal.enums.AttendanceMark;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SetAttendanceRequestDto {

    @NotBlank
    private String studentName;

    @NotBlank
    private String lessonName;

    @NotNull
    private AttendanceMark attendanceMark;
}
