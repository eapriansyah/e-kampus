package id.eara.service;

import id.eara.domain.Zone;
import id.eara.repository.ZoneRepository;
import id.eara.repository.search.ZoneSearchRepository;
import id.eara.service.dto.ZoneDTO;
import id.eara.service.mapper.ZoneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Zone.
 * atiila consulting
 */

@Service
@Transactional
public class ZoneService {

    private final Logger log = LoggerFactory.getLogger(ZoneService.class);

    private final ZoneRepository zoneRepository;

    private final ZoneMapper zoneMapper;

    private final ZoneSearchRepository zoneSearchRepository;

    public ZoneService(ZoneRepository zoneRepository, ZoneMapper zoneMapper, ZoneSearchRepository zoneSearchRepository) {
        this.zoneRepository = zoneRepository;
        this.zoneMapper = zoneMapper;
        this.zoneSearchRepository = zoneSearchRepository;
    }

    /**
     * Save a zone.
     *
     * @param zoneDTO the entity to save
     * @return the persisted entity
     */
    public ZoneDTO save(ZoneDTO zoneDTO) {
        log.debug("Request to save Zone : {}", zoneDTO);
        Zone zone = zoneMapper.toEntity(zoneDTO);
        zone = zoneRepository.save(zone);
        ZoneDTO result = zoneMapper.toDto(zone);
        zoneSearchRepository.save(zone);
        return result;
    }

    /**
     *  Get all the zones.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ZoneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Zones");
        return zoneRepository.findAll(pageable)
            .map(zoneMapper::toDto);
    }

    /**
     *  Get one zone by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ZoneDTO findOne(UUID id) {
        log.debug("Request to get Zone : {}", id);
        Zone zone = zoneRepository.findOne(id);
        return zoneMapper.toDto(zone);
    }

    /**
     *  Delete the  zone by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Zone : {}", id);
        zoneRepository.delete(id);
        zoneSearchRepository.delete(id);
    }

    /**
     * Search for the zone corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ZoneDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Zones for query {}", query);
        Page<Zone> result = zoneSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(zoneMapper::toDto);
    }
}
