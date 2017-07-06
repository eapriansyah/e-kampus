package id.eara.repository;

import id.eara.domain.Lab;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Lab entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface LabRepository extends JpaRepository<Lab,UUID> {


}
