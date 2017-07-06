package id.eara.repository;

import id.eara.domain.RegularCourse;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RegularCourse entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface RegularCourseRepository extends JpaRepository<RegularCourse,UUID> {


}
