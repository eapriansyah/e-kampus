package id.eara.repository;

import id.eara.domain.StudentCourseScore;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudentCourseScore entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentCourseScoreRepository extends JpaRepository<StudentCourseScore,UUID> {


}
