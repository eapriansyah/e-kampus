package id.eara.repository;

import id.eara.domain.ProgramStudy;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProgramStudy entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ProgramStudyRepository extends JpaRepository<ProgramStudy,UUID> {


}
