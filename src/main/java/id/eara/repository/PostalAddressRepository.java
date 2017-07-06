package id.eara.repository;

import id.eara.domain.PostalAddress;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PostalAddress entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface PostalAddressRepository extends JpaRepository<PostalAddress,UUID> {


}
