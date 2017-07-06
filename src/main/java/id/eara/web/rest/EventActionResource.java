package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.EventActionService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.EventActionDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing EventAction.
 */
@RestController
@RequestMapping("/api")
public class EventActionResource {

    private final Logger log = LoggerFactory.getLogger(EventActionResource.class);

    private static final String ENTITY_NAME = "eventAction";

    private final EventActionService eventActionService;

    public EventActionResource(EventActionService eventActionService) {
        this.eventActionService = eventActionService;
    }

    /**
     * POST  /event-actions : Create a new eventAction.
     *
     * @param eventActionDTO the eventActionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new eventActionDTO, or with status 400 (Bad Request) if the eventAction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-actions")
    @Timed
    public ResponseEntity<EventActionDTO> createEventAction(@RequestBody EventActionDTO eventActionDTO) throws URISyntaxException {
        log.debug("REST request to save EventAction : {}", eventActionDTO);
        if (eventActionDTO.getIdEventAction() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new eventAction cannot already have an ID")).body(null);
        }
        EventActionDTO result = eventActionService.save(eventActionDTO);
        
        return ResponseEntity.created(new URI("/api/event-actions/" + result.getIdEventAction()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEventAction().toString()))
            .body(result);
    }

    /**
     * POST  /event-actions/execute : Execute Bussiness Process eventAction.
     *
     * @param eventActionDTO the eventActionDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  eventActionDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/event-actions/execute")
    @Timed
    public ResponseEntity<EventActionDTO> executedEventAction(@RequestBody EventActionDTO eventActionDTO) throws URISyntaxException {
        log.debug("REST request to process EventAction : {}", eventActionDTO);
        return new ResponseEntity<EventActionDTO>(eventActionDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /event-actions : Updates an existing eventAction.
     *
     * @param eventActionDTO the eventActionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated eventActionDTO,
     * or with status 400 (Bad Request) if the eventActionDTO is not valid,
     * or with status 500 (Internal Server Error) if the eventActionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/event-actions")
    @Timed
    public ResponseEntity<EventActionDTO> updateEventAction(@RequestBody EventActionDTO eventActionDTO) throws URISyntaxException {
        log.debug("REST request to update EventAction : {}", eventActionDTO);
        if (eventActionDTO.getIdEventAction() == null) {
            return createEventAction(eventActionDTO);
        }
        EventActionDTO result = eventActionService.save(eventActionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, eventActionDTO.getIdEventAction().toString()))
            .body(result);
    }

    /**
     * GET  /event-actions : get all the eventActions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of eventActions in body
     */
    @GetMapping("/event-actions")
    @Timed
    public ResponseEntity<List<EventActionDTO>> getAllEventActions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of EventActions");
        Page<EventActionDTO> page = eventActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/event-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /event-actions/:id : get the "id" eventAction.
     *
     * @param id the id of the eventActionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the eventActionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/event-actions/{id}")
    @Timed
    public ResponseEntity<EventActionDTO> getEventAction(@PathVariable Long id) {
        log.debug("REST request to get EventAction : {}", id);
        EventActionDTO eventActionDTO = eventActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(eventActionDTO));
    }

    /**
     * DELETE  /event-actions/:id : delete the "id" eventAction.
     *
     * @param id the id of the eventActionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/event-actions/{id}")
    @Timed
    public ResponseEntity<Void> deleteEventAction(@PathVariable Long id) {
        log.debug("REST request to delete EventAction : {}", id);
        eventActionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/event-actions?query=:query : search for the eventAction corresponding
     * to the query.
     *
     * @param query the query of the eventAction search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/event-actions")
    @Timed
    public ResponseEntity<List<EventActionDTO>> searchEventActions(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EventActions for query {}", query);
        Page<EventActionDTO> page = eventActionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/event-actions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
