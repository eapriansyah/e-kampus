package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudentEventService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudentEventDTO;
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
 * REST controller for managing StudentEvent.
 */
@RestController
@RequestMapping("/api")
public class StudentEventResource {

    private final Logger log = LoggerFactory.getLogger(StudentEventResource.class);

    private static final String ENTITY_NAME = "studentEvent";

    private final StudentEventService studentEventService;

    public StudentEventResource(StudentEventService studentEventService) {
        this.studentEventService = studentEventService;
    }

    /**
     * POST  /student-events : Create a new studentEvent.
     *
     * @param studentEventDTO the studentEventDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentEventDTO, or with status 400 (Bad Request) if the studentEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-events")
    @Timed
    public ResponseEntity<StudentEventDTO> createStudentEvent(@RequestBody StudentEventDTO studentEventDTO) throws URISyntaxException {
        log.debug("REST request to save StudentEvent : {}", studentEventDTO);
        if (studentEventDTO.getIdStudentEvent() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentEvent cannot already have an ID")).body(null);
        }
        StudentEventDTO result = studentEventService.save(studentEventDTO);
        
        return ResponseEntity.created(new URI("/api/student-events/" + result.getIdStudentEvent()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudentEvent().toString()))
            .body(result);
    }

    /**
     * POST  /student-events/execute : Execute Bussiness Process studentEvent.
     *
     * @param studentEventDTO the studentEventDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studentEventDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-events/execute")
    @Timed
    public ResponseEntity<StudentEventDTO> executedStudentEvent(@RequestBody StudentEventDTO studentEventDTO) throws URISyntaxException {
        log.debug("REST request to process StudentEvent : {}", studentEventDTO);
        return new ResponseEntity<StudentEventDTO>(studentEventDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /student-events : Updates an existing studentEvent.
     *
     * @param studentEventDTO the studentEventDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentEventDTO,
     * or with status 400 (Bad Request) if the studentEventDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentEventDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-events")
    @Timed
    public ResponseEntity<StudentEventDTO> updateStudentEvent(@RequestBody StudentEventDTO studentEventDTO) throws URISyntaxException {
        log.debug("REST request to update StudentEvent : {}", studentEventDTO);
        if (studentEventDTO.getIdStudentEvent() == null) {
            return createStudentEvent(studentEventDTO);
        }
        StudentEventDTO result = studentEventService.save(studentEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentEventDTO.getIdStudentEvent().toString()))
            .body(result);
    }

    /**
     * GET  /student-events : get all the studentEvents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentEvents in body
     */
    @GetMapping("/student-events")
    @Timed
    public ResponseEntity<List<StudentEventDTO>> getAllStudentEvents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentEvents");
        Page<StudentEventDTO> page = studentEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/student-events/filterBy")
    @Timed
    public ResponseEntity<List<StudentEventDTO>> getAllFilteredStudentEvents(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentEvents");
        Page<StudentEventDTO> page = studentEventService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-events/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /student-events/:id : get the "id" studentEvent.
     *
     * @param id the id of the studentEventDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentEventDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-events/{id}")
    @Timed
    public ResponseEntity<StudentEventDTO> getStudentEvent(@PathVariable Long id) {
        log.debug("REST request to get StudentEvent : {}", id);
        StudentEventDTO studentEventDTO = studentEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentEventDTO));
    }

    /**
     * DELETE  /student-events/:id : delete the "id" studentEvent.
     *
     * @param id the id of the studentEventDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-events/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentEvent(@PathVariable Long id) {
        log.debug("REST request to delete StudentEvent : {}", id);
        studentEventService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-events?query=:query : search for the studentEvent corresponding
     * to the query.
     *
     * @param query the query of the studentEvent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/student-events")
    @Timed
    public ResponseEntity<List<StudentEventDTO>> searchStudentEvents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudentEvents for query {}", query);
        Page<StudentEventDTO> page = studentEventService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/student-events");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
