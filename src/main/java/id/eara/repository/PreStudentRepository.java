package id.eara.repository;

import id.eara.domain.PreStudent;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PreStudent entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PreStudentRepository extends JpaRepository<PreStudent,UUID> {


}
