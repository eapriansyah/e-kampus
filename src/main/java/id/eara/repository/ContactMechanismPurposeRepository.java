package id.eara.repository;

import id.eara.domain.ContactMechanismPurpose;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ContactMechanismPurpose entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ContactMechanismPurposeRepository extends JpaRepository<ContactMechanismPurpose,UUID> {


}
