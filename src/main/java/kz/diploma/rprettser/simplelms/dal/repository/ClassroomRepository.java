package kz.diploma.rprettser.simplelms.dal.repository;

import kz.diploma.rprettser.simplelms.dal.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long>, JpaSpecificationExecutor<Classroom> {
    Optional<Classroom> findByNameAndIsDeletedFalse(String name);
}
