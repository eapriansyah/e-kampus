package id.eara.repository;

import id.eara.domain.Degree;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Degree entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface DegreeRepository extends JpaRepository<Degree,Long> {


}
