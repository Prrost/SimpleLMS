package kz.diploma.rprettser.simplelms.dal.repository;

import kz.diploma.rprettser.simplelms.dal.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Optional<Lesson> findByName(String name);
}
