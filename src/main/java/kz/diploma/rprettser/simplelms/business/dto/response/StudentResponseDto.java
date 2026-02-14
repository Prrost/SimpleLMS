package kz.diploma.rprettser.simplelms.business.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import kz.diploma.rprettser.simplelms.dal.entity.StudentGroup;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponseDto {
    private Long id;

    private String name;

    private String lastName;

    private String email;

    private String phone;

    private Set<StudentGroupShortDto> studentGroups;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    private Boolean isDeleted;
}
