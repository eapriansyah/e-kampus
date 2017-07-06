package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ClassStudyService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ClassStudyDTO;
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
 * REST controller for managing ClassStudy.
 */
@RestController
@RequestMapping("/api")
public class ClassStudyResource {

    private final Logger log = LoggerFactory.getLogger(ClassStudyResource.class);

    private static final String ENTITY_NAME = "classStudy";

    private final ClassStudyService classStudyService;

    public ClassStudyResource(ClassStudyService classStudyService) {
        this.classStudyService = classStudyService;
    }

    /**
     * POST  /class-studies : Create a new classStudy.
     *
     * @param classStudyDTO the classStudyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classStudyDTO, or with status 400 (Bad Request) if the classStudy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-studies")
    @Timed
    public ResponseEntity<ClassStudyDTO> createClassStudy(@RequestBody ClassStudyDTO classStudyDTO) throws URISyntaxException {
        log.debug("REST request to save ClassStudy : {}", classStudyDTO);
        if (classStudyDTO.getIdClassStudy() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new classStudy cannot already have an ID")).body(null);
        }
        ClassStudyDTO result = classStudyService.save(classStudyDTO);
        
        return ResponseEntity.created(new URI("/api/class-studies/" + result.getIdClassStudy()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdClassStudy().toString()))
            .body(result);
    }

    /**
     * POST  /class-studies/execute : Execute Bussiness Process classStudy.
     *
     * @param classStudyDTO the classStudyDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  classStudyDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-studies/execute")
    @Timed
    public ResponseEntity<ClassStudyDTO> executedClassStudy(@RequestBody ClassStudyDTO classStudyDTO) throws URISyntaxException {
        log.debug("REST request to process ClassStudy : {}", classStudyDTO);
        return new ResponseEntity<ClassStudyDTO>(classStudyDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /class-studies : Updates an existing classStudy.
     *
     * @param classStudyDTO the classStudyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classStudyDTO,
     * or with status 400 (Bad Request) if the classStudyDTO is not valid,
     * or with status 500 (Internal Server Error) if the classStudyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/class-studies")
    @Timed
    public ResponseEntity<ClassStudyDTO> updateClassStudy(@RequestBody ClassStudyDTO classStudyDTO) throws URISyntaxException {
        log.debug("REST request to update ClassStudy : {}", classStudyDTO);
        if (classStudyDTO.getIdClassStudy() == null) {
            return createClassStudy(classStudyDTO);
        }
        ClassStudyDTO result = classStudyService.save(classStudyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classStudyDTO.getIdClassStudy().toString()))
            .body(result);
    }

    /**
     * GET  /class-studies : get all the classStudies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of classStudies in body
     */
    @GetMapping("/class-studies")
    @Timed
    public ResponseEntity<List<ClassStudyDTO>> getAllClassStudies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ClassStudies");
        Page<ClassStudyDTO> page = classStudyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/class-studies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/class-studies/filterBy")
    @Timed
    public ResponseEntity<List<ClassStudyDTO>> getAllFilteredClassStudies(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ClassStudies");
        Page<ClassStudyDTO> page = classStudyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/class-studies/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /class-studies/:id : get the "id" classStudy.
     *
     * @param id the id of the classStudyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classStudyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/class-studies/{id}")
    @Timed
    public ResponseEntity<ClassStudyDTO> getClassStudy(@PathVariable UUID id) {
        log.debug("REST request to get ClassStudy : {}", id);
        ClassStudyDTO classStudyDTO = classStudyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classStudyDTO));
    }

    /**
     * DELETE  /class-studies/:id : delete the "id" classStudy.
     *
     * @param id the id of the classStudyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/class-studies/{id}")
    @Timed
    public ResponseEntity<Void> deleteClassStudy(@PathVariable UUID id) {
        log.debug("REST request to delete ClassStudy : {}", id);
        classStudyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/class-studies?query=:query : search for the classStudy corresponding
     * to the query.
     *
     * @param query the query of the classStudy search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/class-studies")
    @Timed
    public ResponseEntity<List<ClassStudyDTO>> searchClassStudies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ClassStudies for query {}", query);
        Page<ClassStudyDTO> page = classStudyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/class-studies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
