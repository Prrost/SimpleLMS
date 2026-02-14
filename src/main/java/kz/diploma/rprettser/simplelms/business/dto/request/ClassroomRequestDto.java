package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassroomRequestDto {

    private Long id;

    private String name;
}
