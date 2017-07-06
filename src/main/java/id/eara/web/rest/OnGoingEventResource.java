package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.OnGoingEventService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.OnGoingEventDTO;
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
 * REST controller for managing OnGoingEvent.
 */
@RestController
@RequestMapping("/api")
public class OnGoingEventResource {

    private final Logger log = LoggerFactory.getLogger(OnGoingEventResource.class);

    private static final String ENTITY_NAME = "onGoingEvent";

    private final OnGoingEventService onGoingEventService;

    public OnGoingEventResource(OnGoingEventService onGoingEventService) {
        this.onGoingEventService = onGoingEventService;
    }

    /**
     * POST  /on-going-events : Create a new onGoingEvent.
     *
     * @param onGoingEventDTO the onGoingEventDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new onGoingEventDTO, or with status 400 (Bad Request) if the onGoingEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/on-going-events")
    @Timed
    public ResponseEntity<OnGoingEventDTO> createOnGoingEvent(@RequestBody OnGoingEventDTO onGoingEventDTO) throws URISyntaxException {
        log.debug("REST request to save OnGoingEvent : {}", onGoingEventDTO);
        if (onGoingEventDTO.getIdEventGo() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new onGoingEvent cannot already have an ID")).body(null);
        }
        OnGoingEventDTO result = onGoingEventService.save(onGoingEventDTO);
        
        return ResponseEntity.created(new URI("/api/on-going-events/" + result.getIdEventGo()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEventGo().toString()))
            .body(result);
    }

    /**
     * POST  /on-going-events/execute : Execute Bussiness Process onGoingEvent.
     *
     * @param onGoingEventDTO the onGoingEventDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  onGoingEventDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/on-going-events/execute")
    @Timed
    public ResponseEntity<OnGoingEventDTO> executedOnGoingEvent(@RequestBody OnGoingEventDTO onGoingEventDTO) throws URISyntaxException {
        log.debug("REST request to process OnGoingEvent : {}", onGoingEventDTO);
        return new ResponseEntity<OnGoingEventDTO>(onGoingEventDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /on-going-events : Updates an existing onGoingEvent.
     *
     * @param onGoingEventDTO the onGoingEventDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated onGoingEventDTO,
     * or with status 400 (Bad Request) if the onGoingEventDTO is not valid,
     * or with status 500 (Internal Server Error) if the onGoingEventDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/on-going-events")
    @Timed
    public ResponseEntity<OnGoingEventDTO> updateOnGoingEvent(@RequestBody OnGoingEventDTO onGoingEventDTO) throws URISyntaxException {
        log.debug("REST request to update OnGoingEvent : {}", onGoingEventDTO);
        if (onGoingEventDTO.getIdEventGo() == null) {
            return createOnGoingEvent(onGoingEventDTO);
        }
        OnGoingEventDTO result = onGoingEventService.save(onGoingEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, onGoingEventDTO.getIdEventGo().toString()))
            .body(result);
    }

    /**
     * GET  /on-going-events : get all the onGoingEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of onGoingEvents in body
     */
    @GetMapping("/on-going-events")
    @Timed
    public ResponseEntity<List<OnGoingEventDTO>> getAllOnGoingEvents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of OnGoingEvents");
        Page<OnGoingEventDTO> page = onGoingEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/on-going-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /on-going-events/:id : get the "id" onGoingEvent.
     *
     * @param id the id of the onGoingEventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the onGoingEventDTO, or with status 404 (Not Found)
     */
    @GetMapping("/on-going-events/{id}")
    @Timed
    public ResponseEntity<OnGoingEventDTO> getOnGoingEvent(@PathVariable UUID id) {
        log.debug("REST request to get OnGoingEvent : {}", id);
        OnGoingEventDTO onGoingEventDTO = onGoingEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(onGoingEventDTO));
    }

    /**
     * DELETE  /on-going-events/:id : delete the "id" onGoingEvent.
     *
     * @param id the id of the onGoingEventDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/on-going-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteOnGoingEvent(@PathVariable UUID id) {
        log.debug("REST request to delete OnGoingEvent : {}", id);
        onGoingEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/on-going-events?query=:query : search for the onGoingEvent corresponding
     * to the query.
     *
     * @param query the query of the onGoingEvent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/on-going-events")
    @Timed
    public ResponseEntity<List<OnGoingEventDTO>> searchOnGoingEvents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of OnGoingEvents for query {}", query);
        Page<OnGoingEventDTO> page = onGoingEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/on-going-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
