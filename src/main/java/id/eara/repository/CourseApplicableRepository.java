package id.eara.repository;

import id.eara.domain.CourseApplicable;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseApplicable entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface CourseApplicableRepository extends JpaRepository<CourseApplicable,UUID> {


}
