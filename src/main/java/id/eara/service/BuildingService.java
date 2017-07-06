package id.eara.service;

import id.eara.domain.Building;
import id.eara.repository.BuildingRepository;
import id.eara.repository.search.BuildingSearchRepository;
import id.eara.service.dto.BuildingDTO;
import id.eara.service.mapper.BuildingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Building.
 * atiila consulting
 */

@Service
@Transactional
public class BuildingService {

    private final Logger log = LoggerFactory.getLogger(BuildingService.class);

    private final BuildingRepository buildingRepository;

    private final BuildingMapper buildingMapper;

    private final BuildingSearchRepository buildingSearchRepository;

    public BuildingService(BuildingRepository buildingRepository, BuildingMapper buildingMapper, BuildingSearchRepository buildingSearchRepository) {
        this.buildingRepository = buildingRepository;
        this.buildingMapper = buildingMapper;
        this.buildingSearchRepository = buildingSearchRepository;
    }

    /**
     * Save a building.
     *
     * @param buildingDTO the entity to save
     * @return the persisted entity
     */
    public BuildingDTO save(BuildingDTO buildingDTO) {
        log.debug("Request to save Building : {}", buildingDTO);
        Building building = buildingMapper.toEntity(buildingDTO);
        building = buildingRepository.save(building);
        BuildingDTO result = buildingMapper.toDto(building);
        buildingSearchRepository.save(building);
        return result;
    }

    /**
     *  Get all the buildings.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BuildingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Buildings");
        return buildingRepository.findAll(pageable)
            .map(buildingMapper::toDto);
    }

    /**
     *  Get one building by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public BuildingDTO findOne(UUID id) {
        log.debug("Request to get Building : {}", id);
        Building building = buildingRepository.findOne(id);
        return buildingMapper.toDto(building);
    }

    /**
     *  Delete the  building by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Building : {}", id);
        buildingRepository.delete(id);
        buildingSearchRepository.delete(id);
    }

    /**
     * Search for the building corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BuildingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Buildings for query {}", query);
        Page<Building> result = buildingSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(buildingMapper::toDto);
    }
}
