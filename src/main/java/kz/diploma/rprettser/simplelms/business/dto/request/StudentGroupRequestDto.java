package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentGroupRequestDto {
    private Long id;

    private String name;

    private Set<Long> studentIds;

    private Boolean isVirtual;

}
