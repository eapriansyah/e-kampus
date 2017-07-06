package id.eara.repository;

import id.eara.domain.AcademicPeriods;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AcademicPeriods entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface AcademicPeriodsRepository extends JpaRepository<AcademicPeriods,UUID> {


}
