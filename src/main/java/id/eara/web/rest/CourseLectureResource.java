package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.CourseLectureService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.CourseLectureDTO;
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
 * REST controller for managing CourseLecture.
 */
@RestController
@RequestMapping("/api")
public class CourseLectureResource {

    private final Logger log = LoggerFactory.getLogger(CourseLectureResource.class);

    private static final String ENTITY_NAME = "courseLecture";

    private final CourseLectureService courseLectureService;

    public CourseLectureResource(CourseLectureService courseLectureService) {
        this.courseLectureService = courseLectureService;
    }

    /**
     * POST  /course-lectures : Create a new courseLecture.
     *
     * @param courseLectureDTO the courseLectureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new courseLectureDTO, or with status 400 (Bad Request) if the courseLecture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-lectures")
    @Timed
    public ResponseEntity<CourseLectureDTO> createCourseLecture(@Valid @RequestBody CourseLectureDTO courseLectureDTO) throws URISyntaxException {
        log.debug("REST request to save CourseLecture : {}", courseLectureDTO);
        if (courseLectureDTO.getIdCourseLecture() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new courseLecture cannot already have an ID")).body(null);
        }
        CourseLectureDTO result = courseLectureService.save(courseLectureDTO);
        
        return ResponseEntity.created(new URI("/api/course-lectures/" + result.getIdCourseLecture()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdCourseLecture().toString()))
            .body(result);
    }

    /**
     * POST  /course-lectures/execute : Execute Bussiness Process courseLecture.
     *
     * @param courseLectureDTO the courseLectureDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  courseLectureDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/course-lectures/execute")
    @Timed
    public ResponseEntity<CourseLectureDTO> executedCourseLecture(@Valid @RequestBody CourseLectureDTO courseLectureDTO) throws URISyntaxException {
        log.debug("REST request to process CourseLecture : {}", courseLectureDTO);
        return new ResponseEntity<CourseLectureDTO>(courseLectureDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /course-lectures : Updates an existing courseLecture.
     *
     * @param courseLectureDTO the courseLectureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated courseLectureDTO,
     * or with status 400 (Bad Request) if the courseLectureDTO is not valid,
     * or with status 500 (Internal Server Error) if the courseLectureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/course-lectures")
    @Timed
    public ResponseEntity<CourseLectureDTO> updateCourseLecture(@Valid @RequestBody CourseLectureDTO courseLectureDTO) throws URISyntaxException {
        log.debug("REST request to update CourseLecture : {}", courseLectureDTO);
        if (courseLectureDTO.getIdCourseLecture() == null) {
            return createCourseLecture(courseLectureDTO);
        }
        CourseLectureDTO result = courseLectureService.save(courseLectureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, courseLectureDTO.getIdCourseLecture().toString()))
            .body(result);
    }

    /**
     * GET  /course-lectures : get all the courseLectures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of courseLectures in body
     */
    @GetMapping("/course-lectures")
    @Timed
    public ResponseEntity<List<CourseLectureDTO>> getAllCourseLectures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of CourseLectures");
        Page<CourseLectureDTO> page = courseLectureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/course-lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /course-lectures/:id : get the "id" courseLecture.
     *
     * @param id the id of the courseLectureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the courseLectureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/course-lectures/{id}")
    @Timed
    public ResponseEntity<CourseLectureDTO> getCourseLecture(@PathVariable UUID id) {
        log.debug("REST request to get CourseLecture : {}", id);
        CourseLectureDTO courseLectureDTO = courseLectureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(courseLectureDTO));
    }

    /**
     * DELETE  /course-lectures/:id : delete the "id" courseLecture.
     *
     * @param id the id of the courseLectureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/course-lectures/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourseLecture(@PathVariable UUID id) {
        log.debug("REST request to delete CourseLecture : {}", id);
        courseLectureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/course-lectures?query=:query : search for the courseLecture corresponding
     * to the query.
     *
     * @param query the query of the courseLecture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/course-lectures")
    @Timed
    public ResponseEntity<List<CourseLectureDTO>> searchCourseLectures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of CourseLectures for query {}", query);
        Page<CourseLectureDTO> page = courseLectureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/course-lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
