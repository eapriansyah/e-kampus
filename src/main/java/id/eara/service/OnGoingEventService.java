package id.eara.service;

import id.eara.domain.OnGoingEvent;
import id.eara.repository.OnGoingEventRepository;
import id.eara.repository.search.OnGoingEventSearchRepository;
import id.eara.service.dto.OnGoingEventDTO;
import id.eara.service.mapper.OnGoingEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing OnGoingEvent.
 * atiila consulting
 */

@Service
@Transactional
public class OnGoingEventService {

    private final Logger log = LoggerFactory.getLogger(OnGoingEventService.class);

    private final OnGoingEventRepository onGoingEventRepository;

    private final OnGoingEventMapper onGoingEventMapper;

    private final OnGoingEventSearchRepository onGoingEventSearchRepository;

    public OnGoingEventService(OnGoingEventRepository onGoingEventRepository, OnGoingEventMapper onGoingEventMapper, OnGoingEventSearchRepository onGoingEventSearchRepository) {
        this.onGoingEventRepository = onGoingEventRepository;
        this.onGoingEventMapper = onGoingEventMapper;
        this.onGoingEventSearchRepository = onGoingEventSearchRepository;
    }

    /**
     * Save a onGoingEvent.
     *
     * @param onGoingEventDTO the entity to save
     * @return the persisted entity
     */
    public OnGoingEventDTO save(OnGoingEventDTO onGoingEventDTO) {
        log.debug("Request to save OnGoingEvent : {}", onGoingEventDTO);
        OnGoingEvent onGoingEvent = onGoingEventMapper.toEntity(onGoingEventDTO);
        onGoingEvent = onGoingEventRepository.save(onGoingEvent);
        OnGoingEventDTO result = onGoingEventMapper.toDto(onGoingEvent);
        onGoingEventSearchRepository.save(onGoingEvent);
        return result;
    }

    /**
     *  Get all the onGoingEvents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OnGoingEventDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OnGoingEvents");
        return onGoingEventRepository.findAll(pageable)
            .map(onGoingEventMapper::toDto);
    }

    /**
     *  Get one onGoingEvent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public OnGoingEventDTO findOne(UUID id) {
        log.debug("Request to get OnGoingEvent : {}", id);
        OnGoingEvent onGoingEvent = onGoingEventRepository.findOne(id);
        return onGoingEventMapper.toDto(onGoingEvent);
    }

    /**
     *  Delete the  onGoingEvent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete OnGoingEvent : {}", id);
        onGoingEventRepository.delete(id);
        onGoingEventSearchRepository.delete(id);
    }

    /**
     * Search for the onGoingEvent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<OnGoingEventDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OnGoingEvents for query {}", query);
        Page<OnGoingEvent> result = onGoingEventSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(onGoingEventMapper::toDto);
    }
}
