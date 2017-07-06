package id.eara.repository;

import id.eara.domain.HostDataSource;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HostDataSource entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface HostDataSourceRepository extends JpaRepository<HostDataSource,UUID> {


}
