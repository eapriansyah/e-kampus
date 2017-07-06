package id.eara.service;

import id.eara.domain.EventAction;
import id.eara.repository.EventActionRepository;
import id.eara.repository.search.EventActionSearchRepository;
import id.eara.service.dto.EventActionDTO;
import id.eara.service.mapper.EventActionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing EventAction.
 * atiila consulting
 */

@Service
@Transactional
public class EventActionService {

    private final Logger log = LoggerFactory.getLogger(EventActionService.class);

    private final EventActionRepository eventActionRepository;

    private final EventActionMapper eventActionMapper;

    private final EventActionSearchRepository eventActionSearchRepository;

    public EventActionService(EventActionRepository eventActionRepository, EventActionMapper eventActionMapper, EventActionSearchRepository eventActionSearchRepository) {
        this.eventActionRepository = eventActionRepository;
        this.eventActionMapper = eventActionMapper;
        this.eventActionSearchRepository = eventActionSearchRepository;
    }

    /**
     * Save a eventAction.
     *
     * @param eventActionDTO the entity to save
     * @return the persisted entity
     */
    public EventActionDTO save(EventActionDTO eventActionDTO) {
        log.debug("Request to save EventAction : {}", eventActionDTO);
        EventAction eventAction = eventActionMapper.toEntity(eventActionDTO);
        eventAction = eventActionRepository.save(eventAction);
        EventActionDTO result = eventActionMapper.toDto(eventAction);
        eventActionSearchRepository.save(eventAction);
        return result;
    }

    /**
     *  Get all the eventActions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventActionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EventActions");
        return eventActionRepository.findAll(pageable)
            .map(eventActionMapper::toDto);
    }

    /**
     *  Get one eventAction by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EventActionDTO findOne(Long id) {
        log.debug("Request to get EventAction : {}", id);
        EventAction eventAction = eventActionRepository.findOne(id);
        return eventActionMapper.toDto(eventAction);
    }

    /**
     *  Delete the  eventAction by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EventAction : {}", id);
        eventActionRepository.delete(id);
        eventActionSearchRepository.delete(id);
    }

    /**
     * Search for the eventAction corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EventActionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EventActions for query {}", query);
        Page<EventAction> result = eventActionSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(eventActionMapper::toDto);
    }
}
