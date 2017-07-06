package id.eara.repository;

import id.eara.domain.ClassStudy;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClassStudy entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ClassStudyRepository extends JpaRepository<ClassStudy,UUID> {


}
