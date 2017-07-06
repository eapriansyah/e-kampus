package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudentPeriodsService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudentPeriodsDTO;
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
 * REST controller for managing StudentPeriods.
 */
@RestController
@RequestMapping("/api")
public class StudentPeriodsResource {

    private final Logger log = LoggerFactory.getLogger(StudentPeriodsResource.class);

    private static final String ENTITY_NAME = "studentPeriods";

    private final StudentPeriodsService studentPeriodsService;

    public StudentPeriodsResource(StudentPeriodsService studentPeriodsService) {
        this.studentPeriodsService = studentPeriodsService;
    }

    /**
     * POST  /student-periods : Create a new studentPeriods.
     *
     * @param studentPeriodsDTO the studentPeriodsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentPeriodsDTO, or with status 400 (Bad Request) if the studentPeriods has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-periods")
    @Timed
    public ResponseEntity<StudentPeriodsDTO> createStudentPeriods(@RequestBody StudentPeriodsDTO studentPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to save StudentPeriods : {}", studentPeriodsDTO);
        if (studentPeriodsDTO.getIdStudentPeriod() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentPeriods cannot already have an ID")).body(null);
        }
        StudentPeriodsDTO result = studentPeriodsService.save(studentPeriodsDTO);
        
        return ResponseEntity.created(new URI("/api/student-periods/" + result.getIdStudentPeriod()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudentPeriod().toString()))
            .body(result);
    }

    /**
     * POST  /student-periods/execute : Execute Bussiness Process studentPeriods.
     *
     * @param studentPeriodsDTO the studentPeriodsDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studentPeriodsDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-periods/execute")
    @Timed
    public ResponseEntity<StudentPeriodsDTO> executedStudentPeriods(@RequestBody StudentPeriodsDTO studentPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to process StudentPeriods : {}", studentPeriodsDTO);
        return new ResponseEntity<StudentPeriodsDTO>(studentPeriodsDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /student-periods : Updates an existing studentPeriods.
     *
     * @param studentPeriodsDTO the studentPeriodsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentPeriodsDTO,
     * or with status 400 (Bad Request) if the studentPeriodsDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentPeriodsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-periods")
    @Timed
    public ResponseEntity<StudentPeriodsDTO> updateStudentPeriods(@RequestBody StudentPeriodsDTO studentPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to update StudentPeriods : {}", studentPeriodsDTO);
        if (studentPeriodsDTO.getIdStudentPeriod() == null) {
            return createStudentPeriods(studentPeriodsDTO);
        }
        StudentPeriodsDTO result = studentPeriodsService.save(studentPeriodsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentPeriodsDTO.getIdStudentPeriod().toString()))
            .body(result);
    }

    /**
     * GET  /student-periods : get all the studentPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentPeriods in body
     */
    @GetMapping("/student-periods")
    @Timed
    public ResponseEntity<List<StudentPeriodsDTO>> getAllStudentPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentPeriods");
        Page<StudentPeriodsDTO> page = studentPeriodsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/student-periods/filterBy")
    @Timed
    public ResponseEntity<List<StudentPeriodsDTO>> getAllFilteredStudentPeriods(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentPeriods");
        Page<StudentPeriodsDTO> page = studentPeriodsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-periods/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /student-periods/:id : get the "id" studentPeriods.
     *
     * @param id the id of the studentPeriodsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentPeriodsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-periods/{id}")
    @Timed
    public ResponseEntity<StudentPeriodsDTO> getStudentPeriods(@PathVariable UUID id) {
        log.debug("REST request to get StudentPeriods : {}", id);
        StudentPeriodsDTO studentPeriodsDTO = studentPeriodsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentPeriodsDTO));
    }

    /**
     * DELETE  /student-periods/:id : delete the "id" studentPeriods.
     *
     * @param id the id of the studentPeriodsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentPeriods(@PathVariable UUID id) {
        log.debug("REST request to delete StudentPeriods : {}", id);
        studentPeriodsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-periods?query=:query : search for the studentPeriods corresponding
     * to the query.
     *
     * @param query the query of the studentPeriods search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/student-periods")
    @Timed
    public ResponseEntity<List<StudentPeriodsDTO>> searchStudentPeriods(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudentPeriods for query {}", query);
        Page<StudentPeriodsDTO> page = studentPeriodsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/student-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
