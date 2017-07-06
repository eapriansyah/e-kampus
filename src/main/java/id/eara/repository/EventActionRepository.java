package id.eara.repository;

import id.eara.domain.EventAction;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EventAction entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface EventActionRepository extends JpaRepository<EventAction,Long> {


}
