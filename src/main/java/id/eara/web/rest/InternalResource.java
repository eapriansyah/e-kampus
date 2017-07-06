package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.InternalService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.InternalDTO;
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
 * REST controller for managing Internal.
 */
@RestController
@RequestMapping("/api")
public class InternalResource {

    private final Logger log = LoggerFactory.getLogger(InternalResource.class);

    private static final String ENTITY_NAME = "internal";

    private final InternalService internalService;

    public InternalResource(InternalService internalService) {
        this.internalService = internalService;
    }

    /**
     * POST  /internals : Create a new internal.
     *
     * @param internalDTO the internalDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new internalDTO, or with status 400 (Bad Request) if the internal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/internals")
    @Timed
    public ResponseEntity<InternalDTO> createInternal(@RequestBody InternalDTO internalDTO) throws URISyntaxException {
        log.debug("REST request to save Internal : {}", internalDTO);
        if (internalDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new internal cannot already have an ID")).body(null);
        }
        InternalDTO result = internalService.save(internalDTO);
        
        return ResponseEntity.created(new URI("/api/internals/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /internals/execute : Execute Bussiness Process internal.
     *
     * @param internalDTO the internalDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  internalDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/internals/execute")
    @Timed
    public ResponseEntity<InternalDTO> executedInternal(@RequestBody InternalDTO internalDTO) throws URISyntaxException {
        log.debug("REST request to process Internal : {}", internalDTO);
        return new ResponseEntity<InternalDTO>(internalDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /internals : Updates an existing internal.
     *
     * @param internalDTO the internalDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated internalDTO,
     * or with status 400 (Bad Request) if the internalDTO is not valid,
     * or with status 500 (Internal Server Error) if the internalDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/internals")
    @Timed
    public ResponseEntity<InternalDTO> updateInternal(@RequestBody InternalDTO internalDTO) throws URISyntaxException {
        log.debug("REST request to update Internal : {}", internalDTO);
        if (internalDTO.getIdPartyRole() == null) {
            return createInternal(internalDTO);
        }
        InternalDTO result = internalService.save(internalDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, internalDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /internals : get all the internals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of internals in body
     */
    @GetMapping("/internals")
    @Timed
    public ResponseEntity<List<InternalDTO>> getAllInternals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Internals");
        Page<InternalDTO> page = internalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/internals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /internals/:id : get the "id" internal.
     *
     * @param id the id of the internalDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the internalDTO, or with status 404 (Not Found)
     */
    @GetMapping("/internals/{id}")
    @Timed
    public ResponseEntity<InternalDTO> getInternal(@PathVariable UUID id) {
        log.debug("REST request to get Internal : {}", id);
        InternalDTO internalDTO = internalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(internalDTO));
    }

    /**
     * DELETE  /internals/:id : delete the "id" internal.
     *
     * @param id the id of the internalDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/internals/{id}")
    @Timed
    public ResponseEntity<Void> deleteInternal(@PathVariable UUID id) {
        log.debug("REST request to delete Internal : {}", id);
        internalService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/internals?query=:query : search for the internal corresponding
     * to the query.
     *
     * @param query the query of the internal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/internals")
    @Timed
    public ResponseEntity<List<InternalDTO>> searchInternals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Internals for query {}", query);
        Page<InternalDTO> page = internalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/internals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
