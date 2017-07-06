package id.eara.repository;

import id.eara.domain.ElectronicAddress;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ElectronicAddress entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ElectronicAddressRepository extends JpaRepository<ElectronicAddress,UUID> {


}
