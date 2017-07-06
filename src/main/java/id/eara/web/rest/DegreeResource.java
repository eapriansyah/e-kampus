package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.DegreeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.DegreeDTO;
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
 * REST controller for managing Degree.
 */
@RestController
@RequestMapping("/api")
public class DegreeResource {

    private final Logger log = LoggerFactory.getLogger(DegreeResource.class);

    private static final String ENTITY_NAME = "degree";

    private final DegreeService degreeService;

    public DegreeResource(DegreeService degreeService) {
        this.degreeService = degreeService;
    }

    /**
     * POST  /degrees : Create a new degree.
     *
     * @param degreeDTO the degreeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new degreeDTO, or with status 400 (Bad Request) if the degree has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/degrees")
    @Timed
    public ResponseEntity<DegreeDTO> createDegree(@RequestBody DegreeDTO degreeDTO) throws URISyntaxException {
        log.debug("REST request to save Degree : {}", degreeDTO);
        if (degreeDTO.getIdDegree() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new degree cannot already have an ID")).body(null);
        }
        DegreeDTO result = degreeService.save(degreeDTO);
        
        return ResponseEntity.created(new URI("/api/degrees/" + result.getIdDegree()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdDegree().toString()))
            .body(result);
    }

    /**
     * POST  /degrees/execute : Execute Bussiness Process degree.
     *
     * @param degreeDTO the degreeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  degreeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/degrees/execute")
    @Timed
    public ResponseEntity<DegreeDTO> executedDegree(@RequestBody DegreeDTO degreeDTO) throws URISyntaxException {
        log.debug("REST request to process Degree : {}", degreeDTO);
        return new ResponseEntity<DegreeDTO>(degreeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /degrees : Updates an existing degree.
     *
     * @param degreeDTO the degreeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated degreeDTO,
     * or with status 400 (Bad Request) if the degreeDTO is not valid,
     * or with status 500 (Internal Server Error) if the degreeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/degrees")
    @Timed
    public ResponseEntity<DegreeDTO> updateDegree(@RequestBody DegreeDTO degreeDTO) throws URISyntaxException {
        log.debug("REST request to update Degree : {}", degreeDTO);
        if (degreeDTO.getIdDegree() == null) {
            return createDegree(degreeDTO);
        }
        DegreeDTO result = degreeService.save(degreeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, degreeDTO.getIdDegree().toString()))
            .body(result);
    }

    /**
     * GET  /degrees : get all the degrees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of degrees in body
     */
    @GetMapping("/degrees")
    @Timed
    public ResponseEntity<List<DegreeDTO>> getAllDegrees(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Degrees");
        Page<DegreeDTO> page = degreeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/degrees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /degrees/:id : get the "id" degree.
     *
     * @param id the id of the degreeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the degreeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/degrees/{id}")
    @Timed
    public ResponseEntity<DegreeDTO> getDegree(@PathVariable Long id) {
        log.debug("REST request to get Degree : {}", id);
        DegreeDTO degreeDTO = degreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(degreeDTO));
    }

    /**
     * DELETE  /degrees/:id : delete the "id" degree.
     *
     * @param id the id of the degreeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/degrees/{id}")
    @Timed
    public ResponseEntity<Void> deleteDegree(@PathVariable Long id) {
        log.debug("REST request to delete Degree : {}", id);
        degreeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/degrees?query=:query : search for the degree corresponding
     * to the query.
     *
     * @param query the query of the degree search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/degrees")
    @Timed
    public ResponseEntity<List<DegreeDTO>> searchDegrees(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Degrees for query {}", query);
        Page<DegreeDTO> page = degreeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/degrees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
