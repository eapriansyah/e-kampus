package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.FacultyService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.FacultyDTO;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Faculty.
 */
@RestController
@RequestMapping("/api")
public class FacultyResource {

    private final Logger log = LoggerFactory.getLogger(FacultyResource.class);

    private static final String ENTITY_NAME = "faculty";

    private final FacultyService facultyService;

    public FacultyResource(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    /**
     * POST  /faculties : Create a new faculty.
     *
     * @param facultyDTO the facultyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new facultyDTO, or with status 400 (Bad Request) if the faculty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/faculties")
    @Timed
    public ResponseEntity<FacultyDTO> createFaculty(@Valid @RequestBody FacultyDTO facultyDTO) throws URISyntaxException {
        log.debug("REST request to save Faculty : {}", facultyDTO);
        if (facultyDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new faculty cannot already have an ID")).body(null);
        }
        FacultyDTO result = facultyService.save(facultyDTO);
        
        return ResponseEntity.created(new URI("/api/faculties/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /faculties/execute : Execute Bussiness Process faculty.
     *
     * @param facultyDTO the facultyDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  facultyDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/faculties/execute")
    @Timed
    public ResponseEntity<FacultyDTO> executedFaculty(@Valid @RequestBody FacultyDTO facultyDTO) throws URISyntaxException {
        log.debug("REST request to process Faculty : {}", facultyDTO);
        return new ResponseEntity<FacultyDTO>(facultyDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /faculties : Updates an existing faculty.
     *
     * @param facultyDTO the facultyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated facultyDTO,
     * or with status 400 (Bad Request) if the facultyDTO is not valid,
     * or with status 500 (Internal Server Error) if the facultyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/faculties")
    @Timed
    public ResponseEntity<FacultyDTO> updateFaculty(@Valid @RequestBody FacultyDTO facultyDTO) throws URISyntaxException {
        log.debug("REST request to update Faculty : {}", facultyDTO);
        if (facultyDTO.getIdPartyRole() == null) {
            return createFaculty(facultyDTO);
        }
        FacultyDTO result = facultyService.save(facultyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, facultyDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /faculties : get all the faculties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of faculties in body
     */
    @GetMapping("/faculties")
    @Timed
    public ResponseEntity<List<FacultyDTO>> getAllFaculties(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Faculties");
        Page<FacultyDTO> page = facultyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/faculties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /faculties/:id : get the "id" faculty.
     *
     * @param id the id of the facultyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the facultyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/faculties/{id}")
    @Timed
    public ResponseEntity<FacultyDTO> getFaculty(@PathVariable UUID id) {
        log.debug("REST request to get Faculty : {}", id);
        FacultyDTO facultyDTO = facultyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(facultyDTO));
    }

    /**
     * DELETE  /faculties/:id : delete the "id" faculty.
     *
     * @param id the id of the facultyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/faculties/{id}")
    @Timed
    public ResponseEntity<Void> deleteFaculty(@PathVariable UUID id) {
        log.debug("REST request to delete Faculty : {}", id);
        facultyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/faculties?query=:query : search for the faculty corresponding
     * to the query.
     *
     * @param query the query of the faculty search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/faculties")
    @Timed
    public ResponseEntity<List<FacultyDTO>> searchFaculties(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Faculties for query {}", query);
        Page<FacultyDTO> page = facultyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/faculties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
