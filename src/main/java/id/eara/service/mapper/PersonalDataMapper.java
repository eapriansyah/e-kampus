package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.PersonalDataDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity PersonalData and its DTO PersonalDataDTO.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class, ReligionTypeMapper.class, EducationTypeMapper.class, WorkTypeMapper.class, })
public interface PersonalDataMapper extends EntityMapper <PersonalDataDTO, PersonalData> {

    @Mapping(source = "person.idParty", target = "personId")
    @Mapping(source = "person.firstName", target = "personFirstName")

    @Mapping(source = "motherReligion.idReligionType", target = "motherReligionId")
    @Mapping(source = "motherReligion.description", target = "motherReligionDescription")

    @Mapping(source = "fatherReligion.idReligionType", target = "fatherReligionId")
    @Mapping(source = "fatherReligion.description", target = "fatherReligionDescription")

    @Mapping(source = "fatherEducation.idEducationType", target = "fatherEducationId")
    @Mapping(source = "fatherEducation.description", target = "fatherEducationDescription")

    @Mapping(source = "motherEducation.idEducationType", target = "motherEducationId")
    @Mapping(source = "motherEducation.description", target = "motherEducationDescription")

    @Mapping(source = "fatherWorkType.idWorkType", target = "fatherWorkTypeId")
    @Mapping(source = "fatherWorkType.description", target = "fatherWorkTypeDescription")

    @Mapping(source = "motherWorkType.idWorkType", target = "motherWorkTypeId")
    @Mapping(source = "motherWorkType.description", target = "motherWorkTypeDescription")
    PersonalDataDTO toDto(PersonalData personalData); 

    @Mapping(source = "personId", target = "person")

    @Mapping(source = "motherReligionId", target = "motherReligion")

    @Mapping(source = "fatherReligionId", target = "fatherReligion")

    @Mapping(source = "fatherEducationId", target = "fatherEducation")

    @Mapping(source = "motherEducationId", target = "motherEducation")

    @Mapping(source = "fatherWorkTypeId", target = "fatherWorkType")

    @Mapping(source = "motherWorkTypeId", target = "motherWorkType")
    PersonalData toEntity(PersonalDataDTO personalDataDTO); 

    default PersonalData fromId(UUID id) {
        if (id == null) {
            return null;
        }
        PersonalData personalData = new PersonalData();
        personalData.setIdPersonalData(id);
        return personalData;
    }
}
