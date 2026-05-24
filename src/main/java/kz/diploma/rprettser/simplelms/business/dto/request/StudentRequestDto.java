package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRequestDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    private String email;

    private String phone;

    private Set<Long> studentGroupsIds;
}
