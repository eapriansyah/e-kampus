package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.ExtraCourseDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity ExtraCourse and its DTO ExtraCourseDTO.
 */
@Mapper(componentModel = "spring", uses = {InternalMapper.class, })
public interface ExtraCourseMapper extends EntityMapper <ExtraCourseDTO, ExtraCourse> {

    @Mapping(source = "owner.idPartyRole", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    ExtraCourseDTO toDto(ExtraCourse extraCourse); 

    @Mapping(source = "ownerId", target = "owner")
    ExtraCourse toEntity(ExtraCourseDTO extraCourseDTO); 

    default ExtraCourse fromId(UUID id) {
        if (id == null) {
            return null;
        }
        ExtraCourse extraCourse = new ExtraCourse();
        extraCourse.setIdCourse(id);
        return extraCourse;
    }
}
