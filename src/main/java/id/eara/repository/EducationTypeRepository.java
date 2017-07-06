package id.eara.repository;

import id.eara.domain.EducationType;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EducationType entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface EducationTypeRepository extends JpaRepository<EducationType,Long> {


}
