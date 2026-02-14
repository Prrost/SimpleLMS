package kz.diploma.rprettser.simplelms.business.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentRequestDto {

    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private Set<Long> studentGroupsIds;
}
