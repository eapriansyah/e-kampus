package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudentCoursePeriodService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudentCoursePeriodDTO;
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
 * REST controller for managing StudentCoursePeriod.
 */
@RestController
@RequestMapping("/api")
public class StudentCoursePeriodResource {

    private final Logger log = LoggerFactory.getLogger(StudentCoursePeriodResource.class);

    private static final String ENTITY_NAME = "studentCoursePeriod";

    private final StudentCoursePeriodService studentCoursePeriodService;

    public StudentCoursePeriodResource(StudentCoursePeriodService studentCoursePeriodService) {
        this.studentCoursePeriodService = studentCoursePeriodService;
    }

    /**
     * POST  /student-course-periods : Create a new studentCoursePeriod.
     *
     * @param studentCoursePeriodDTO the studentCoursePeriodDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentCoursePeriodDTO, or with status 400 (Bad Request) if the studentCoursePeriod has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-course-periods")
    @Timed
    public ResponseEntity<StudentCoursePeriodDTO> createStudentCoursePeriod(@RequestBody StudentCoursePeriodDTO studentCoursePeriodDTO) throws URISyntaxException {
        log.debug("REST request to save StudentCoursePeriod : {}", studentCoursePeriodDTO);
        if (studentCoursePeriodDTO.getIdStudentCoursePeriod() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentCoursePeriod cannot already have an ID")).body(null);
        }
        StudentCoursePeriodDTO result = studentCoursePeriodService.save(studentCoursePeriodDTO);
        
        return ResponseEntity.created(new URI("/api/student-course-periods/" + result.getIdStudentCoursePeriod()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudentCoursePeriod().toString()))
            .body(result);
    }

    /**
     * POST  /student-course-periods/execute : Execute Bussiness Process studentCoursePeriod.
     *
     * @param studentCoursePeriodDTO the studentCoursePeriodDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studentCoursePeriodDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-course-periods/execute")
    @Timed
    public ResponseEntity<StudentCoursePeriodDTO> executedStudentCoursePeriod(@RequestBody StudentCoursePeriodDTO studentCoursePeriodDTO) throws URISyntaxException {
        log.debug("REST request to process StudentCoursePeriod : {}", studentCoursePeriodDTO);
        return new ResponseEntity<StudentCoursePeriodDTO>(studentCoursePeriodDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /student-course-periods : Updates an existing studentCoursePeriod.
     *
     * @param studentCoursePeriodDTO the studentCoursePeriodDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentCoursePeriodDTO,
     * or with status 400 (Bad Request) if the studentCoursePeriodDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentCoursePeriodDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-course-periods")
    @Timed
    public ResponseEntity<StudentCoursePeriodDTO> updateStudentCoursePeriod(@RequestBody StudentCoursePeriodDTO studentCoursePeriodDTO) throws URISyntaxException {
        log.debug("REST request to update StudentCoursePeriod : {}", studentCoursePeriodDTO);
        if (studentCoursePeriodDTO.getIdStudentCoursePeriod() == null) {
            return createStudentCoursePeriod(studentCoursePeriodDTO);
        }
        StudentCoursePeriodDTO result = studentCoursePeriodService.save(studentCoursePeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentCoursePeriodDTO.getIdStudentCoursePeriod().toString()))
            .body(result);
    }

    /**
     * GET  /student-course-periods : get all the studentCoursePeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentCoursePeriods in body
     */
    @GetMapping("/student-course-periods")
    @Timed
    public ResponseEntity<List<StudentCoursePeriodDTO>> getAllStudentCoursePeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentCoursePeriods");
        Page<StudentCoursePeriodDTO> page = studentCoursePeriodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-course-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/student-course-periods/filterBy")
    @Timed
    public ResponseEntity<List<StudentCoursePeriodDTO>> getAllFilteredStudentCoursePeriods(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentCoursePeriods");
        Page<StudentCoursePeriodDTO> page = studentCoursePeriodService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-course-periods/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /student-course-periods/:id : get the "id" studentCoursePeriod.
     *
     * @param id the id of the studentCoursePeriodDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentCoursePeriodDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-course-periods/{id}")
    @Timed
    public ResponseEntity<StudentCoursePeriodDTO> getStudentCoursePeriod(@PathVariable UUID id) {
        log.debug("REST request to get StudentCoursePeriod : {}", id);
        StudentCoursePeriodDTO studentCoursePeriodDTO = studentCoursePeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentCoursePeriodDTO));
    }

    /**
     * DELETE  /student-course-periods/:id : delete the "id" studentCoursePeriod.
     *
     * @param id the id of the studentCoursePeriodDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-course-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentCoursePeriod(@PathVariable UUID id) {
        log.debug("REST request to delete StudentCoursePeriod : {}", id);
        studentCoursePeriodService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-course-periods?query=:query : search for the studentCoursePeriod corresponding
     * to the query.
     *
     * @param query the query of the studentCoursePeriod search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/student-course-periods")
    @Timed
    public ResponseEntity<List<StudentCoursePeriodDTO>> searchStudentCoursePeriods(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudentCoursePeriods for query {}", query);
        Page<StudentCoursePeriodDTO> page = studentCoursePeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/student-course-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
