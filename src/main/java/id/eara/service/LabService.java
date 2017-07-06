package id.eara.service;

import id.eara.domain.Lab;
import id.eara.repository.LabRepository;
import id.eara.repository.search.LabSearchRepository;
import id.eara.service.dto.LabDTO;
import id.eara.service.mapper.LabMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Lab.
 * atiila consulting
 */

@Service
@Transactional
public class LabService {

    private final Logger log = LoggerFactory.getLogger(LabService.class);

    private final LabRepository labRepository;

    private final LabMapper labMapper;

    private final LabSearchRepository labSearchRepository;

    public LabService(LabRepository labRepository, LabMapper labMapper, LabSearchRepository labSearchRepository) {
        this.labRepository = labRepository;
        this.labMapper = labMapper;
        this.labSearchRepository = labSearchRepository;
    }

    /**
     * Save a lab.
     *
     * @param labDTO the entity to save
     * @return the persisted entity
     */
    public LabDTO save(LabDTO labDTO) {
        log.debug("Request to save Lab : {}", labDTO);
        Lab lab = labMapper.toEntity(labDTO);
        lab = labRepository.save(lab);
        LabDTO result = labMapper.toDto(lab);
        labSearchRepository.save(lab);
        return result;
    }

    /**
     *  Get all the labs.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LabDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Labs");
        return labRepository.findAll(pageable)
            .map(labMapper::toDto);
    }

    /**
     *  Get one lab by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public LabDTO findOne(UUID id) {
        log.debug("Request to get Lab : {}", id);
        Lab lab = labRepository.findOne(id);
        return labMapper.toDto(lab);
    }

    /**
     *  Delete the  lab by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Lab : {}", id);
        labRepository.delete(id);
        labSearchRepository.delete(id);
    }

    /**
     * Search for the lab corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<LabDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Labs for query {}", query);
        Page<Lab> result = labSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(labMapper::toDto);
    }
}
