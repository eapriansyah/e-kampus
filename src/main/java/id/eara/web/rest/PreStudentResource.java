package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.PreStudentService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PreStudentDTO;
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
 * REST controller for managing PreStudent.
 */
@RestController
@RequestMapping("/api")
public class PreStudentResource {

    private final Logger log = LoggerFactory.getLogger(PreStudentResource.class);

    private static final String ENTITY_NAME = "preStudent";

    private final PreStudentService preStudentService;

    public PreStudentResource(PreStudentService preStudentService) {
        this.preStudentService = preStudentService;
    }

    /**
     * POST  /pre-students : Create a new preStudent.
     *
     * @param preStudentDTO the preStudentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new preStudentDTO, or with status 400 (Bad Request) if the preStudent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pre-students")
    @Timed
    public ResponseEntity<PreStudentDTO> createPreStudent(@RequestBody PreStudentDTO preStudentDTO) throws URISyntaxException {
        log.debug("REST request to save PreStudent : {}", preStudentDTO);
        if (preStudentDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new preStudent cannot already have an ID")).body(null);
        }
        PreStudentDTO result = preStudentService.save(preStudentDTO);
        
        return ResponseEntity.created(new URI("/api/pre-students/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /pre-students/execute : Execute Bussiness Process preStudent.
     *
     * @param preStudentDTO the preStudentDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  preStudentDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pre-students/execute")
    @Timed
    public ResponseEntity<PreStudentDTO> executedPreStudent(@RequestBody PreStudentDTO preStudentDTO) throws URISyntaxException {
        log.debug("REST request to process PreStudent : {}", preStudentDTO);
        return new ResponseEntity<PreStudentDTO>(preStudentDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /pre-students : Updates an existing preStudent.
     *
     * @param preStudentDTO the preStudentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated preStudentDTO,
     * or with status 400 (Bad Request) if the preStudentDTO is not valid,
     * or with status 500 (Internal Server Error) if the preStudentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pre-students")
    @Timed
    public ResponseEntity<PreStudentDTO> updatePreStudent(@RequestBody PreStudentDTO preStudentDTO) throws URISyntaxException {
        log.debug("REST request to update PreStudent : {}", preStudentDTO);
        if (preStudentDTO.getIdPartyRole() == null) {
            return createPreStudent(preStudentDTO);
        }
        PreStudentDTO result = preStudentService.save(preStudentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, preStudentDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /pre-students : get all the preStudents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of preStudents in body
     */
    @GetMapping("/pre-students")
    @Timed
    public ResponseEntity<List<PreStudentDTO>> getAllPreStudents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PreStudents");
        Page<PreStudentDTO> page = preStudentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pre-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /pre-students/:id : get the "id" preStudent.
     *
     * @param id the id of the preStudentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the preStudentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pre-students/{id}")
    @Timed
    public ResponseEntity<PreStudentDTO> getPreStudent(@PathVariable UUID id) {
        log.debug("REST request to get PreStudent : {}", id);
        PreStudentDTO preStudentDTO = preStudentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(preStudentDTO));
    }

    /**
     * DELETE  /pre-students/:id : delete the "id" preStudent.
     *
     * @param id the id of the preStudentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pre-students/{id}")
    @Timed
    public ResponseEntity<Void> deletePreStudent(@PathVariable UUID id) {
        log.debug("REST request to delete PreStudent : {}", id);
        preStudentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pre-students?query=:query : search for the preStudent corresponding
     * to the query.
     *
     * @param query the query of the preStudent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/pre-students")
    @Timed
    public ResponseEntity<List<PreStudentDTO>> searchPreStudents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PreStudents for query {}", query);
        Page<PreStudentDTO> page = preStudentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/pre-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
