package id.eara.repository;

import id.eara.domain.Faculty;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Faculty entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface FacultyRepository extends JpaRepository<Faculty,UUID> {


}
