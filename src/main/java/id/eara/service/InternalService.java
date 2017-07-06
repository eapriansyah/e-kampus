package id.eara.service;

import id.eara.domain.Internal;
import id.eara.repository.InternalRepository;
import id.eara.repository.search.InternalSearchRepository;
import id.eara.service.dto.InternalDTO;
import id.eara.service.mapper.InternalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Internal.
 * atiila consulting
 */

@Service
@Transactional
public class InternalService {

    private final Logger log = LoggerFactory.getLogger(InternalService.class);

    private final InternalRepository internalRepository;

    private final InternalMapper internalMapper;

    private final InternalSearchRepository internalSearchRepository;

    public InternalService(InternalRepository internalRepository, InternalMapper internalMapper, InternalSearchRepository internalSearchRepository) {
        this.internalRepository = internalRepository;
        this.internalMapper = internalMapper;
        this.internalSearchRepository = internalSearchRepository;
    }

    /**
     * Save a internal.
     *
     * @param internalDTO the entity to save
     * @return the persisted entity
     */
    public InternalDTO save(InternalDTO internalDTO) {
        log.debug("Request to save Internal : {}", internalDTO);
        Internal internal = internalMapper.toEntity(internalDTO);
        internal = internalRepository.save(internal);
        InternalDTO result = internalMapper.toDto(internal);
        internalSearchRepository.save(internal);
        return result;
    }

    /**
     *  Get all the internals.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InternalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Internals");
        return internalRepository.findAll(pageable)
            .map(internalMapper::toDto);
    }

    /**
     *  Get one internal by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public InternalDTO findOne(UUID id) {
        log.debug("Request to get Internal : {}", id);
        Internal internal = internalRepository.findOne(id);
        return internalMapper.toDto(internal);
    }

    /**
     *  Delete the  internal by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Internal : {}", id);
        internalRepository.delete(id);
        internalSearchRepository.delete(id);
    }

    /**
     * Search for the internal corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InternalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Internals for query {}", query);
        Page<Internal> result = internalSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(internalMapper::toDto);
    }
}
