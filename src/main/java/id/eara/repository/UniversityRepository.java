package id.eara.repository;

import id.eara.domain.University;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the University entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface UniversityRepository extends JpaRepository<University,UUID> {


}
