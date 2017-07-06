package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.RegularCourseService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.RegularCourseDTO;
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
 * REST controller for managing RegularCourse.
 */
@RestController
@RequestMapping("/api")
public class RegularCourseResource {

    private final Logger log = LoggerFactory.getLogger(RegularCourseResource.class);

    private static final String ENTITY_NAME = "regularCourse";

    private final RegularCourseService regularCourseService;

    public RegularCourseResource(RegularCourseService regularCourseService) {
        this.regularCourseService = regularCourseService;
    }

    /**
     * POST  /regular-courses : Create a new regularCourse.
     *
     * @param regularCourseDTO the regularCourseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new regularCourseDTO, or with status 400 (Bad Request) if the regularCourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/regular-courses")
    @Timed
    public ResponseEntity<RegularCourseDTO> createRegularCourse(@RequestBody RegularCourseDTO regularCourseDTO) throws URISyntaxException {
        log.debug("REST request to save RegularCourse : {}", regularCourseDTO);
        if (regularCourseDTO.getIdCourse() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new regularCourse cannot already have an ID")).body(null);
        }
        RegularCourseDTO result = regularCourseService.save(regularCourseDTO);
        
        return ResponseEntity.created(new URI("/api/regular-courses/" + result.getIdCourse()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCourse().toString()))
            .body(result);
    }

    /**
     * POST  /regular-courses/execute : Execute Bussiness Process regularCourse.
     *
     * @param regularCourseDTO the regularCourseDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  regularCourseDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/regular-courses/execute")
    @Timed
    public ResponseEntity<RegularCourseDTO> executedRegularCourse(@RequestBody RegularCourseDTO regularCourseDTO) throws URISyntaxException {
        log.debug("REST request to process RegularCourse : {}", regularCourseDTO);
        return new ResponseEntity<RegularCourseDTO>(regularCourseDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /regular-courses : Updates an existing regularCourse.
     *
     * @param regularCourseDTO the regularCourseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated regularCourseDTO,
     * or with status 400 (Bad Request) if the regularCourseDTO is not valid,
     * or with status 500 (Internal Server Error) if the regularCourseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/regular-courses")
    @Timed
    public ResponseEntity<RegularCourseDTO> updateRegularCourse(@RequestBody RegularCourseDTO regularCourseDTO) throws URISyntaxException {
        log.debug("REST request to update RegularCourse : {}", regularCourseDTO);
        if (regularCourseDTO.getIdCourse() == null) {
            return createRegularCourse(regularCourseDTO);
        }
        RegularCourseDTO result = regularCourseService.save(regularCourseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, regularCourseDTO.getIdCourse().toString()))
            .body(result);
    }

    /**
     * GET  /regular-courses : get all the regularCourses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of regularCourses in body
     */
    @GetMapping("/regular-courses")
    @Timed
    public ResponseEntity<List<RegularCourseDTO>> getAllRegularCourses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of RegularCourses");
        Page<RegularCourseDTO> page = regularCourseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/regular-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /regular-courses/:id : get the "id" regularCourse.
     *
     * @param id the id of the regularCourseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the regularCourseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/regular-courses/{id}")
    @Timed
    public ResponseEntity<RegularCourseDTO> getRegularCourse(@PathVariable UUID id) {
        log.debug("REST request to get RegularCourse : {}", id);
        RegularCourseDTO regularCourseDTO = regularCourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(regularCourseDTO));
    }

    /**
     * DELETE  /regular-courses/:id : delete the "id" regularCourse.
     *
     * @param id the id of the regularCourseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/regular-courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteRegularCourse(@PathVariable UUID id) {
        log.debug("REST request to delete RegularCourse : {}", id);
        regularCourseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/regular-courses?query=:query : search for the regularCourse corresponding
     * to the query.
     *
     * @param query the query of the regularCourse search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/regular-courses")
    @Timed
    public ResponseEntity<List<RegularCourseDTO>> searchRegularCourses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of RegularCourses for query {}", query);
        Page<RegularCourseDTO> page = regularCourseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/regular-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
