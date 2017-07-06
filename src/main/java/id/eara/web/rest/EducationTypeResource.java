package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.EducationTypeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.EducationTypeDTO;
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
 * REST controller for managing EducationType.
 */
@RestController
@RequestMapping("/api")
public class EducationTypeResource {

    private final Logger log = LoggerFactory.getLogger(EducationTypeResource.class);

    private static final String ENTITY_NAME = "educationType";

    private final EducationTypeService educationTypeService;

    public EducationTypeResource(EducationTypeService educationTypeService) {
        this.educationTypeService = educationTypeService;
    }

    /**
     * POST  /education-types : Create a new educationType.
     *
     * @param educationTypeDTO the educationTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new educationTypeDTO, or with status 400 (Bad Request) if the educationType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/education-types")
    @Timed
    public ResponseEntity<EducationTypeDTO> createEducationType(@RequestBody EducationTypeDTO educationTypeDTO) throws URISyntaxException {
        log.debug("REST request to save EducationType : {}", educationTypeDTO);
        if (educationTypeDTO.getIdEducationType() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new educationType cannot already have an ID")).body(null);
        }
        EducationTypeDTO result = educationTypeService.save(educationTypeDTO);
        
        return ResponseEntity.created(new URI("/api/education-types/" + result.getIdEducationType()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdEducationType().toString()))
            .body(result);
    }

    /**
     * POST  /education-types/execute : Execute Bussiness Process educationType.
     *
     * @param educationTypeDTO the educationTypeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  educationTypeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/education-types/execute")
    @Timed
    public ResponseEntity<EducationTypeDTO> executedEducationType(@RequestBody EducationTypeDTO educationTypeDTO) throws URISyntaxException {
        log.debug("REST request to process EducationType : {}", educationTypeDTO);
        return new ResponseEntity<EducationTypeDTO>(educationTypeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /education-types : Updates an existing educationType.
     *
     * @param educationTypeDTO the educationTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated educationTypeDTO,
     * or with status 400 (Bad Request) if the educationTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the educationTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/education-types")
    @Timed
    public ResponseEntity<EducationTypeDTO> updateEducationType(@RequestBody EducationTypeDTO educationTypeDTO) throws URISyntaxException {
        log.debug("REST request to update EducationType : {}", educationTypeDTO);
        if (educationTypeDTO.getIdEducationType() == null) {
            return createEducationType(educationTypeDTO);
        }
        EducationTypeDTO result = educationTypeService.save(educationTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, educationTypeDTO.getIdEducationType().toString()))
            .body(result);
    }

    /**
     * GET  /education-types : get all the educationTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of educationTypes in body
     */
    @GetMapping("/education-types")
    @Timed
    public ResponseEntity<List<EducationTypeDTO>> getAllEducationTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of EducationTypes");
        Page<EducationTypeDTO> page = educationTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/education-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /education-types/:id : get the "id" educationType.
     *
     * @param id the id of the educationTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the educationTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/education-types/{id}")
    @Timed
    public ResponseEntity<EducationTypeDTO> getEducationType(@PathVariable Long id) {
        log.debug("REST request to get EducationType : {}", id);
        EducationTypeDTO educationTypeDTO = educationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(educationTypeDTO));
    }

    /**
     * DELETE  /education-types/:id : delete the "id" educationType.
     *
     * @param id the id of the educationTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/education-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteEducationType(@PathVariable Long id) {
        log.debug("REST request to delete EducationType : {}", id);
        educationTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/education-types?query=:query : search for the educationType corresponding
     * to the query.
     *
     * @param query the query of the educationType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/education-types")
    @Timed
    public ResponseEntity<List<EducationTypeDTO>> searchEducationTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of EducationTypes for query {}", query);
        Page<EducationTypeDTO> page = educationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/education-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
