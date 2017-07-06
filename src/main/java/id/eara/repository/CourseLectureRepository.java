package id.eara.repository;

import id.eara.domain.CourseLecture;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CourseLecture entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface CourseLectureRepository extends JpaRepository<CourseLecture,UUID> {


}
