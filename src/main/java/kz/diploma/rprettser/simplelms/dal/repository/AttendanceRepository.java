package kz.diploma.rprettser.simplelms.dal.repository;

import kz.diploma.rprettser.simplelms.dal.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long>, JpaSpecificationExecutor<Attendance> {
    Optional<Attendance> findFirstByStudentIdAndLessonId(Long studentId, Long lessonId);
}
