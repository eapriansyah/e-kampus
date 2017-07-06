package id.eara.repository;

import id.eara.domain.OnGoingEvent;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OnGoingEvent entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface OnGoingEventRepository extends JpaRepository<OnGoingEvent,UUID> {


}
