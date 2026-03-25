package kz.diploma.rprettser.simplelms.dal.repository;

import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {
    Optional<Lesson> findByName(String name);
}
