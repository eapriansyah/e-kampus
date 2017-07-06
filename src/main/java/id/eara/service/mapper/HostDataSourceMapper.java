package id.eara.service.mapper;

import id.eara.domain.*;
import id.eara.service.dto.HostDataSourceDTO;

import org.mapstruct.*;

import java.util.UUID;


/**
 * Mapper for the entity HostDataSource and its DTO HostDataSourceDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgramStudyMapper.class, StudyPathMapper.class, })
public interface HostDataSourceMapper extends EntityMapper <HostDataSourceDTO, HostDataSource> {

    @Mapping(source = "prody.idPartyRole", target = "prodyId")
    @Mapping(source = "prody.name", target = "prodyName")

    @Mapping(source = "studyPath.idStudyPath", target = "studyPathId")
    @Mapping(source = "studyPath.description", target = "studyPathDescription")
    HostDataSourceDTO toDto(HostDataSource hostDataSource); 

    @Mapping(source = "prodyId", target = "prody")

    @Mapping(source = "studyPathId", target = "studyPath")
    HostDataSource toEntity(HostDataSourceDTO hostDataSourceDTO); 

    default HostDataSource fromId(UUID id) {
        if (id == null) {
            return null;
        }
        HostDataSource hostDataSource = new HostDataSource();
        hostDataSource.setIdHostDataSource(id);
        return hostDataSource;
    }
}
