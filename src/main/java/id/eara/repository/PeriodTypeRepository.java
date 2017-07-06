package id.eara.repository;

import id.eara.domain.PeriodType;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PeriodType entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodTypeRepository extends JpaRepository<PeriodType,Long> {


}
