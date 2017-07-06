package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.RegularCourseDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity RegularCourse and its DTO RegularCourseDTO.
 */
@Mapper(componentModel = "spring", uses = {InternalMapper.class, })
public interface RegularCourseMapper extends EntityMapper <RegularCourseDTO, RegularCourse> {

    @Mapping(source = "owner.idPartyRole", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    RegularCourseDTO toDto(RegularCourse regularCourse); 

    @Mapping(source = "ownerId", target = "owner")
    RegularCourse toEntity(RegularCourseDTO regularCourseDTO); 

    default RegularCourse fromId(UUID id) {
        if (id == null) {
            return null;
        }
        RegularCourse regularCourse = new RegularCourse();
        regularCourse.setIdCourse(id);
        return regularCourse;
    }
}
