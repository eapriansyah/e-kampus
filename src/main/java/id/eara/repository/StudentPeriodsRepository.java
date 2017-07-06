package id.eara.repository;

import id.eara.domain.StudentPeriods;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudentPeriods entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudentPeriodsRepository extends JpaRepository<StudentPeriods,UUID> {


}
