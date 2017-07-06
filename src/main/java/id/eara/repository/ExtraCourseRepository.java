package id.eara.repository;

import id.eara.domain.ExtraCourse;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ExtraCourse entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ExtraCourseRepository extends JpaRepository<ExtraCourse,UUID> {


}
