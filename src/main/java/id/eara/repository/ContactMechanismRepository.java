package id.eara.repository;

import id.eara.domain.ContactMechanism;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContactMechanism entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ContactMechanismRepository extends JpaRepository<ContactMechanism,UUID> {


}
