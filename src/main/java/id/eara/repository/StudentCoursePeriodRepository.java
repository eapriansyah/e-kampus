package id.eara.repository;

import id.eara.domain.StudentCoursePeriod;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudentCoursePeriod entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentCoursePeriodRepository extends JpaRepository<StudentCoursePeriod,UUID> {


}
