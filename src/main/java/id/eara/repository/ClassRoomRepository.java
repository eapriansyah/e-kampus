package id.eara.repository;

import id.eara.domain.ClassRoom;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClassRoom entity.
 * atiila consulting
 */
@SuppressWarnings("unused")
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom,UUID> {


}
