package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.TelecomunicationNumberService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.TelecomunicationNumberDTO;
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
 * REST controller for managing TelecomunicationNumber.
 */
@RestController
@RequestMapping("/api")
public class TelecomunicationNumberResource {

    private final Logger log = LoggerFactory.getLogger(TelecomunicationNumberResource.class);

    private static final String ENTITY_NAME = "telecomunicationNumber";

    private final TelecomunicationNumberService telecomunicationNumberService;

    public TelecomunicationNumberResource(TelecomunicationNumberService telecomunicationNumberService) {
        this.telecomunicationNumberService = telecomunicationNumberService;
    }

    /**
     * POST  /telecomunication-numbers : Create a new telecomunicationNumber.
     *
     * @param telecomunicationNumberDTO the telecomunicationNumberDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telecomunicationNumberDTO, or with status 400 (Bad Request) if the telecomunicationNumber has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telecomunication-numbers")
    @Timed
    public ResponseEntity<TelecomunicationNumberDTO> createTelecomunicationNumber(@RequestBody TelecomunicationNumberDTO telecomunicationNumberDTO) throws URISyntaxException {
        log.debug("REST request to save TelecomunicationNumber : {}", telecomunicationNumberDTO);
        if (telecomunicationNumberDTO.getIdContact() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new telecomunicationNumber cannot already have an ID")).body(null);
        }
        TelecomunicationNumberDTO result = telecomunicationNumberService.save(telecomunicationNumberDTO);
        
        return ResponseEntity.created(new URI("/api/telecomunication-numbers/" + result.getIdContact()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContact().toString()))
            .body(result);
    }

    /**
     * POST  /telecomunication-numbers/execute : Execute Bussiness Process telecomunicationNumber.
     *
     * @param telecomunicationNumberDTO the telecomunicationNumberDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  telecomunicationNumberDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/telecomunication-numbers/execute")
    @Timed
    public ResponseEntity<TelecomunicationNumberDTO> executedTelecomunicationNumber(@RequestBody TelecomunicationNumberDTO telecomunicationNumberDTO) throws URISyntaxException {
        log.debug("REST request to process TelecomunicationNumber : {}", telecomunicationNumberDTO);
        return new ResponseEntity<TelecomunicationNumberDTO>(telecomunicationNumberDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /telecomunication-numbers : Updates an existing telecomunicationNumber.
     *
     * @param telecomunicationNumberDTO the telecomunicationNumberDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telecomunicationNumberDTO,
     * or with status 400 (Bad Request) if the telecomunicationNumberDTO is not valid,
     * or with status 500 (Internal Server Error) if the telecomunicationNumberDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/telecomunication-numbers")
    @Timed
    public ResponseEntity<TelecomunicationNumberDTO> updateTelecomunicationNumber(@RequestBody TelecomunicationNumberDTO telecomunicationNumberDTO) throws URISyntaxException {
        log.debug("REST request to update TelecomunicationNumber : {}", telecomunicationNumberDTO);
        if (telecomunicationNumberDTO.getIdContact() == null) {
            return createTelecomunicationNumber(telecomunicationNumberDTO);
        }
        TelecomunicationNumberDTO result = telecomunicationNumberService.save(telecomunicationNumberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, telecomunicationNumberDTO.getIdContact().toString()))
            .body(result);
    }

    /**
     * GET  /telecomunication-numbers : get all the telecomunicationNumbers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of telecomunicationNumbers in body
     */
    @GetMapping("/telecomunication-numbers")
    @Timed
    public ResponseEntity<List<TelecomunicationNumberDTO>> getAllTelecomunicationNumbers(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of TelecomunicationNumbers");
        Page<TelecomunicationNumberDTO> page = telecomunicationNumberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/telecomunication-numbers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /telecomunication-numbers/:id : get the "id" telecomunicationNumber.
     *
     * @param id the id of the telecomunicationNumberDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telecomunicationNumberDTO, or with status 404 (Not Found)
     */
    @GetMapping("/telecomunication-numbers/{id}")
    @Timed
    public ResponseEntity<TelecomunicationNumberDTO> getTelecomunicationNumber(@PathVariable UUID id) {
        log.debug("REST request to get TelecomunicationNumber : {}", id);
        TelecomunicationNumberDTO telecomunicationNumberDTO = telecomunicationNumberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(telecomunicationNumberDTO));
    }

    /**
     * DELETE  /telecomunication-numbers/:id : delete the "id" telecomunicationNumber.
     *
     * @param id the id of the telecomunicationNumberDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/telecomunication-numbers/{id}")
    @Timed
    public ResponseEntity<Void> deleteTelecomunicationNumber(@PathVariable UUID id) {
        log.debug("REST request to delete TelecomunicationNumber : {}", id);
        telecomunicationNumberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/telecomunication-numbers?query=:query : search for the telecomunicationNumber corresponding
     * to the query.
     *
     * @param query the query of the telecomunicationNumber search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/telecomunication-numbers")
    @Timed
    public ResponseEntity<List<TelecomunicationNumberDTO>> searchTelecomunicationNumbers(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of TelecomunicationNumbers for query {}", query);
        Page<TelecomunicationNumberDTO> page = telecomunicationNumberService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/telecomunication-numbers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
