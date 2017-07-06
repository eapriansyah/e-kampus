package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.UniversityService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.UniversityDTO;
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
 * REST controller for managing University.
 */
@RestController
@RequestMapping("/api")
public class UniversityResource {

    private final Logger log = LoggerFactory.getLogger(UniversityResource.class);

    private static final String ENTITY_NAME = "university";

    private final UniversityService universityService;

    public UniversityResource(UniversityService universityService) {
        this.universityService = universityService;
    }

    /**
     * POST  /universities : Create a new university.
     *
     * @param universityDTO the universityDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new universityDTO, or with status 400 (Bad Request) if the university has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/universities")
    @Timed
    public ResponseEntity<UniversityDTO> createUniversity(@RequestBody UniversityDTO universityDTO) throws URISyntaxException {
        log.debug("REST request to save University : {}", universityDTO);
        if (universityDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new university cannot already have an ID")).body(null);
        }
        UniversityDTO result = universityService.save(universityDTO);
        
        return ResponseEntity.created(new URI("/api/universities/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /universities/execute : Execute Bussiness Process university.
     *
     * @param universityDTO the universityDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  universityDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/universities/execute")
    @Timed
    public ResponseEntity<UniversityDTO> executedUniversity(@RequestBody UniversityDTO universityDTO) throws URISyntaxException {
        log.debug("REST request to process University : {}", universityDTO);
        return new ResponseEntity<UniversityDTO>(universityDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /universities : Updates an existing university.
     *
     * @param universityDTO the universityDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated universityDTO,
     * or with status 400 (Bad Request) if the universityDTO is not valid,
     * or with status 500 (Internal Server Error) if the universityDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/universities")
    @Timed
    public ResponseEntity<UniversityDTO> updateUniversity(@RequestBody UniversityDTO universityDTO) throws URISyntaxException {
        log.debug("REST request to update University : {}", universityDTO);
        if (universityDTO.getIdPartyRole() == null) {
            return createUniversity(universityDTO);
        }
        UniversityDTO result = universityService.save(universityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, universityDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /universities : get all the universities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of universities in body
     */
    @GetMapping("/universities")
    @Timed
    public ResponseEntity<List<UniversityDTO>> getAllUniversities(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Universities");
        Page<UniversityDTO> page = universityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/universities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /universities/:id : get the "id" university.
     *
     * @param id the id of the universityDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the universityDTO, or with status 404 (Not Found)
     */
    @GetMapping("/universities/{id}")
    @Timed
    public ResponseEntity<UniversityDTO> getUniversity(@PathVariable UUID id) {
        log.debug("REST request to get University : {}", id);
        UniversityDTO universityDTO = universityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(universityDTO));
    }

    /**
     * DELETE  /universities/:id : delete the "id" university.
     *
     * @param id the id of the universityDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/universities/{id}")
    @Timed
    public ResponseEntity<Void> deleteUniversity(@PathVariable UUID id) {
        log.debug("REST request to delete University : {}", id);
        universityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/universities?query=:query : search for the university corresponding
     * to the query.
     *
     * @param query the query of the university search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/universities")
    @Timed
    public ResponseEntity<List<UniversityDTO>> searchUniversities(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Universities for query {}", query);
        Page<UniversityDTO> page = universityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/universities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
