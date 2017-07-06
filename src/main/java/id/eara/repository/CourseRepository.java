package id.eara.repository;

import id.eara.domain.Course;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Course entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course,UUID> {


}
