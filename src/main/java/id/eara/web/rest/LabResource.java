package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.LabService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.LabDTO;
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
 * REST controller for managing Lab.
 */
@RestController
@RequestMapping("/api")
public class LabResource {

    private final Logger log = LoggerFactory.getLogger(LabResource.class);

    private static final String ENTITY_NAME = "lab";

    private final LabService labService;

    public LabResource(LabService labService) {
        this.labService = labService;
    }

    /**
     * POST  /labs : Create a new lab.
     *
     * @param labDTO the labDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new labDTO, or with status 400 (Bad Request) if the lab has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/labs")
    @Timed
    public ResponseEntity<LabDTO> createLab(@RequestBody LabDTO labDTO) throws URISyntaxException {
        log.debug("REST request to save Lab : {}", labDTO);
        if (labDTO.getIdFacility() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lab cannot already have an ID")).body(null);
        }
        LabDTO result = labService.save(labDTO);
        
        return ResponseEntity.created(new URI("/api/labs/" + result.getIdFacility()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdFacility().toString()))
            .body(result);
    }

    /**
     * POST  /labs/execute : Execute Bussiness Process lab.
     *
     * @param labDTO the labDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  labDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/labs/execute")
    @Timed
    public ResponseEntity<LabDTO> executedLab(@RequestBody LabDTO labDTO) throws URISyntaxException {
        log.debug("REST request to process Lab : {}", labDTO);
        return new ResponseEntity<LabDTO>(labDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /labs : Updates an existing lab.
     *
     * @param labDTO the labDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated labDTO,
     * or with status 400 (Bad Request) if the labDTO is not valid,
     * or with status 500 (Internal Server Error) if the labDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/labs")
    @Timed
    public ResponseEntity<LabDTO> updateLab(@RequestBody LabDTO labDTO) throws URISyntaxException {
        log.debug("REST request to update Lab : {}", labDTO);
        if (labDTO.getIdFacility() == null) {
            return createLab(labDTO);
        }
        LabDTO result = labService.save(labDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, labDTO.getIdFacility().toString()))
            .body(result);
    }

    /**
     * GET  /labs : get all the labs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of labs in body
     */
    @GetMapping("/labs")
    @Timed
    public ResponseEntity<List<LabDTO>> getAllLabs(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Labs");
        Page<LabDTO> page = labService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/labs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /labs/:id : get the "id" lab.
     *
     * @param id the id of the labDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the labDTO, or with status 404 (Not Found)
     */
    @GetMapping("/labs/{id}")
    @Timed
    public ResponseEntity<LabDTO> getLab(@PathVariable UUID id) {
        log.debug("REST request to get Lab : {}", id);
        LabDTO labDTO = labService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(labDTO));
    }

    /**
     * DELETE  /labs/:id : delete the "id" lab.
     *
     * @param id the id of the labDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/labs/{id}")
    @Timed
    public ResponseEntity<Void> deleteLab(@PathVariable UUID id) {
        log.debug("REST request to delete Lab : {}", id);
        labService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/labs?query=:query : search for the lab corresponding
     * to the query.
     *
     * @param query the query of the lab search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/labs")
    @Timed
    public ResponseEntity<List<LabDTO>> searchLabs(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Labs for query {}", query);
        Page<LabDTO> page = labService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/labs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
