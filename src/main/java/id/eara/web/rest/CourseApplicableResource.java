package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.CourseApplicableService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.CourseApplicableDTO;
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
 * REST controller for managing CourseApplicable.
 */
@RestController
@RequestMapping("/api")
public class CourseApplicableResource {

    private final Logger log = LoggerFactory.getLogger(CourseApplicableResource.class);

    private static final String ENTITY_NAME = "courseApplicable";

    private final CourseApplicableService courseApplicableService;

    public CourseApplicableResource(CourseApplicableService courseApplicableService) {
        this.courseApplicableService = courseApplicableService;
    }

    /**
     * POST  /course-applicables : Create a new courseApplicable.
     *
     * @param courseApplicableDTO the courseApplicableDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseApplicableDTO, or with status 400 (Bad Request) if the courseApplicable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-applicables")
    @Timed
    public ResponseEntity<CourseApplicableDTO> createCourseApplicable(@Valid @RequestBody CourseApplicableDTO courseApplicableDTO) throws URISyntaxException {
        log.debug("REST request to save CourseApplicable : {}", courseApplicableDTO);
        if (courseApplicableDTO.getIdApplCourse() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseApplicable cannot already have an ID")).body(null);
        }
        CourseApplicableDTO result = courseApplicableService.save(courseApplicableDTO);
        
        return ResponseEntity.created(new URI("/api/course-applicables/" + result.getIdApplCourse()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdApplCourse().toString()))
            .body(result);
    }

    /**
     * POST  /course-applicables/execute : Execute Bussiness Process courseApplicable.
     *
     * @param courseApplicableDTO the courseApplicableDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  courseApplicableDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-applicables/execute")
    @Timed
    public ResponseEntity<CourseApplicableDTO> executedCourseApplicable(@Valid @RequestBody CourseApplicableDTO courseApplicableDTO) throws URISyntaxException {
        log.debug("REST request to process CourseApplicable : {}", courseApplicableDTO);
        return new ResponseEntity<CourseApplicableDTO>(courseApplicableDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /course-applicables : Updates an existing courseApplicable.
     *
     * @param courseApplicableDTO the courseApplicableDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseApplicableDTO,
     * or with status 400 (Bad Request) if the courseApplicableDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseApplicableDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-applicables")
    @Timed
    public ResponseEntity<CourseApplicableDTO> updateCourseApplicable(@Valid @RequestBody CourseApplicableDTO courseApplicableDTO) throws URISyntaxException {
        log.debug("REST request to update CourseApplicable : {}", courseApplicableDTO);
        if (courseApplicableDTO.getIdApplCourse() == null) {
            return createCourseApplicable(courseApplicableDTO);
        }
        CourseApplicableDTO result = courseApplicableService.save(courseApplicableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseApplicableDTO.getIdApplCourse().toString()))
            .body(result);
    }

    /**
     * GET  /course-applicables : get all the courseApplicables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseApplicables in body
     */
    @GetMapping("/course-applicables")
    @Timed
    public ResponseEntity<List<CourseApplicableDTO>> getAllCourseApplicables(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseApplicables");
        Page<CourseApplicableDTO> page = courseApplicableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-applicables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/course-applicables/filterBy")
    @Timed
    public ResponseEntity<List<CourseApplicableDTO>> getAllFilteredCourseApplicables(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseApplicables");
        Page<CourseApplicableDTO> page = courseApplicableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-applicables/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /course-applicables/:id : get the "id" courseApplicable.
     *
     * @param id the id of the courseApplicableDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseApplicableDTO, or with status 404 (Not Found)
     */
    @GetMapping("/course-applicables/{id}")
    @Timed
    public ResponseEntity<CourseApplicableDTO> getCourseApplicable(@PathVariable UUID id) {
        log.debug("REST request to get CourseApplicable : {}", id);
        CourseApplicableDTO courseApplicableDTO = courseApplicableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseApplicableDTO));
    }

    /**
     * DELETE  /course-applicables/:id : delete the "id" courseApplicable.
     *
     * @param id the id of the courseApplicableDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-applicables/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseApplicable(@PathVariable UUID id) {
        log.debug("REST request to delete CourseApplicable : {}", id);
        courseApplicableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/course-applicables?query=:query : search for the courseApplicable corresponding
     * to the query.
     *
     * @param query the query of the courseApplicable search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/course-applicables")
    @Timed
    public ResponseEntity<List<CourseApplicableDTO>> searchCourseApplicables(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CourseApplicables for query {}", query);
        Page<CourseApplicableDTO> page = courseApplicableService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/course-applicables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
