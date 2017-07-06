package id.eara.repository;

import id.eara.domain.StudyPath;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StudyPath entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface StudyPathRepository extends JpaRepository<StudyPath,Long> {


}
