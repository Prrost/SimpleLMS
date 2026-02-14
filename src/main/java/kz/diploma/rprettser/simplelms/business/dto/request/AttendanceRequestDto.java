package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import kz.diploma.rprettser.simplelms.dal.enums.AttendanceMark;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendanceRequestDto {

    private Long id;

    private Long studentId;

    private Long lessonId;

    private String studentName;

    private String lessonName;

    private AttendanceMark attendanceMark;
}
