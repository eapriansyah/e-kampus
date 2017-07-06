package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ExtraCourseService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ExtraCourseDTO;
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
 * REST controller for managing ExtraCourse.
 */
@RestController
@RequestMapping("/api")
public class ExtraCourseResource {

    private final Logger log = LoggerFactory.getLogger(ExtraCourseResource.class);

    private static final String ENTITY_NAME = "extraCourse";

    private final ExtraCourseService extraCourseService;

    public ExtraCourseResource(ExtraCourseService extraCourseService) {
        this.extraCourseService = extraCourseService;
    }

    /**
     * POST  /extra-courses : Create a new extraCourse.
     *
     * @param extraCourseDTO the extraCourseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extraCourseDTO, or with status 400 (Bad Request) if the extraCourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-courses")
    @Timed
    public ResponseEntity<ExtraCourseDTO> createExtraCourse(@RequestBody ExtraCourseDTO extraCourseDTO) throws URISyntaxException {
        log.debug("REST request to save ExtraCourse : {}", extraCourseDTO);
        if (extraCourseDTO.getIdCourse() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new extraCourse cannot already have an ID")).body(null);
        }
        ExtraCourseDTO result = extraCourseService.save(extraCourseDTO);
        
        return ResponseEntity.created(new URI("/api/extra-courses/" + result.getIdCourse()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCourse().toString()))
            .body(result);
    }

    /**
     * POST  /extra-courses/execute : Execute Bussiness Process extraCourse.
     *
     * @param extraCourseDTO the extraCourseDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  extraCourseDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extra-courses/execute")
    @Timed
    public ResponseEntity<ExtraCourseDTO> executedExtraCourse(@RequestBody ExtraCourseDTO extraCourseDTO) throws URISyntaxException {
        log.debug("REST request to process ExtraCourse : {}", extraCourseDTO);
        return new ResponseEntity<ExtraCourseDTO>(extraCourseDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /extra-courses : Updates an existing extraCourse.
     *
     * @param extraCourseDTO the extraCourseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extraCourseDTO,
     * or with status 400 (Bad Request) if the extraCourseDTO is not valid,
     * or with status 500 (Internal Server Error) if the extraCourseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extra-courses")
    @Timed
    public ResponseEntity<ExtraCourseDTO> updateExtraCourse(@RequestBody ExtraCourseDTO extraCourseDTO) throws URISyntaxException {
        log.debug("REST request to update ExtraCourse : {}", extraCourseDTO);
        if (extraCourseDTO.getIdCourse() == null) {
            return createExtraCourse(extraCourseDTO);
        }
        ExtraCourseDTO result = extraCourseService.save(extraCourseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, extraCourseDTO.getIdCourse().toString()))
            .body(result);
    }

    /**
     * GET  /extra-courses : get all the extraCourses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of extraCourses in body
     */
    @GetMapping("/extra-courses")
    @Timed
    public ResponseEntity<List<ExtraCourseDTO>> getAllExtraCourses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ExtraCourses");
        Page<ExtraCourseDTO> page = extraCourseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/extra-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /extra-courses/:id : get the "id" extraCourse.
     *
     * @param id the id of the extraCourseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extraCourseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/extra-courses/{id}")
    @Timed
    public ResponseEntity<ExtraCourseDTO> getExtraCourse(@PathVariable UUID id) {
        log.debug("REST request to get ExtraCourse : {}", id);
        ExtraCourseDTO extraCourseDTO = extraCourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(extraCourseDTO));
    }

    /**
     * DELETE  /extra-courses/:id : delete the "id" extraCourse.
     *
     * @param id the id of the extraCourseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extra-courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtraCourse(@PathVariable UUID id) {
        log.debug("REST request to delete ExtraCourse : {}", id);
        extraCourseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/extra-courses?query=:query : search for the extraCourse corresponding
     * to the query.
     *
     * @param query the query of the extraCourse search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/extra-courses")
    @Timed
    public ResponseEntity<List<ExtraCourseDTO>> searchExtraCourses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ExtraCourses for query {}", query);
        Page<ExtraCourseDTO> page = extraCourseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/extra-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
