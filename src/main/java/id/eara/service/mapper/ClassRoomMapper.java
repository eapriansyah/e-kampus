package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ClassRoomDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ClassRoom and its DTO ClassRoomDTO.
 */
@Mapper(componentModel = "spring", uses = {BuildingMapper.class, })
public interface ClassRoomMapper extends EntityMapper <ClassRoomDTO, ClassRoom> {

    @Mapping(source = "building.idFacility", target = "buildingId")
    @Mapping(source = "building.description", target = "buildingDescription")
    ClassRoomDTO toDto(ClassRoom classRoom); 

    @Mapping(source = "buildingId", target = "building")
    ClassRoom toEntity(ClassRoomDTO classRoomDTO); 

    default ClassRoom fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ClassRoom classRoom = new ClassRoom();
        classRoom.setIdFacility(id);
        return classRoom;
    }
}
