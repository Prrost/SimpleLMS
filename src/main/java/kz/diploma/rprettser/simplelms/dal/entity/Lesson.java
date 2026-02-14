package kz.diploma.rprettser.simplelms.dal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name =  "id")
    private Long id;

    @Column(name =  "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;

    @Column(name =  "starts_at")
    private LocalDateTime startsAt;

    @Column(name =  "ends_at")
    private LocalDateTime endsAt;

    @Column(name =  "expires_at")
    private LocalDateTime expiresAt;

    @Column(name =  "created_at")
    private LocalDateTime createdAt;

    @Column(name =  "updated_at")
    private LocalDateTime updatedAt;

    @Column(name =  "created_by")
    private String createdBy;

    @Column(name =  "updated_by")
    private String updatedBy;

    @Column(name =  "is_deleted")
    private Boolean isDeleted = false;
}
