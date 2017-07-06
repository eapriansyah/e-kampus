package id.eara.repository;

import id.eara.domain.Student;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Student entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRepository extends JpaRepository<Student,UUID> {


}
