package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudentPeriodDataService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudentPeriodDataDTO;
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
 * REST controller for managing StudentPeriodData.
 */
@RestController
@RequestMapping("/api")
public class StudentPeriodDataResource {

    private final Logger log = LoggerFactory.getLogger(StudentPeriodDataResource.class);

    private static final String ENTITY_NAME = "studentPeriodData";

    private final StudentPeriodDataService studentPeriodDataService;

    public StudentPeriodDataResource(StudentPeriodDataService studentPeriodDataService) {
        this.studentPeriodDataService = studentPeriodDataService;
    }

    /**
     * POST  /student-period-data : Create a new studentPeriodData.
     *
     * @param studentPeriodDataDTO the studentPeriodDataDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentPeriodDataDTO, or with status 400 (Bad Request) if the studentPeriodData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-period-data")
    @Timed
    public ResponseEntity<StudentPeriodDataDTO> createStudentPeriodData(@RequestBody StudentPeriodDataDTO studentPeriodDataDTO) throws URISyntaxException {
        log.debug("REST request to save StudentPeriodData : {}", studentPeriodDataDTO);
        if (studentPeriodDataDTO.getIdStudentPeriod() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentPeriodData cannot already have an ID")).body(null);
        }
        StudentPeriodDataDTO result = studentPeriodDataService.save(studentPeriodDataDTO);
        
        return ResponseEntity.created(new URI("/api/student-period-data/" + result.getIdStudentPeriod()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudentPeriod().toString()))
            .body(result);
    }

    /**
     * POST  /student-period-data/execute : Execute Bussiness Process studentPeriodData.
     *
     * @param studentPeriodDataDTO the studentPeriodDataDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studentPeriodDataDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-period-data/execute")
    @Timed
    public ResponseEntity<StudentPeriodDataDTO> executedStudentPeriodData(@RequestBody StudentPeriodDataDTO studentPeriodDataDTO) throws URISyntaxException {
        log.debug("REST request to process StudentPeriodData : {}", studentPeriodDataDTO);
        return new ResponseEntity<StudentPeriodDataDTO>(studentPeriodDataDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /student-period-data : Updates an existing studentPeriodData.
     *
     * @param studentPeriodDataDTO the studentPeriodDataDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentPeriodDataDTO,
     * or with status 400 (Bad Request) if the studentPeriodDataDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentPeriodDataDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-period-data")
    @Timed
    public ResponseEntity<StudentPeriodDataDTO> updateStudentPeriodData(@RequestBody StudentPeriodDataDTO studentPeriodDataDTO) throws URISyntaxException {
        log.debug("REST request to update StudentPeriodData : {}", studentPeriodDataDTO);
        if (studentPeriodDataDTO.getIdStudentPeriod() == null) {
            return createStudentPeriodData(studentPeriodDataDTO);
        }
        StudentPeriodDataDTO result = studentPeriodDataService.save(studentPeriodDataDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentPeriodDataDTO.getIdStudentPeriod().toString()))
            .body(result);
    }

    /**
     * GET  /student-period-data : get all the studentPeriodData.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentPeriodData in body
     */
    @GetMapping("/student-period-data")
    @Timed
    public ResponseEntity<List<StudentPeriodDataDTO>> getAllStudentPeriodData(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentPeriodData");
        Page<StudentPeriodDataDTO> page = studentPeriodDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-period-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/student-period-data/filterBy")
    @Timed
    public ResponseEntity<List<StudentPeriodDataDTO>> getAllFilteredStudentPeriodData(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentPeriodData");
        Page<StudentPeriodDataDTO> page = studentPeriodDataService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-period-data/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /student-period-data/:id : get the "id" studentPeriodData.
     *
     * @param id the id of the studentPeriodDataDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentPeriodDataDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-period-data/{id}")
    @Timed
    public ResponseEntity<StudentPeriodDataDTO> getStudentPeriodData(@PathVariable UUID id) {
        log.debug("REST request to get StudentPeriodData : {}", id);
        StudentPeriodDataDTO studentPeriodDataDTO = studentPeriodDataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentPeriodDataDTO));
    }

    /**
     * DELETE  /student-period-data/:id : delete the "id" studentPeriodData.
     *
     * @param id the id of the studentPeriodDataDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-period-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentPeriodData(@PathVariable UUID id) {
        log.debug("REST request to delete StudentPeriodData : {}", id);
        studentPeriodDataService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-period-data?query=:query : search for the studentPeriodData corresponding
     * to the query.
     *
     * @param query the query of the studentPeriodData search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/student-period-data")
    @Timed
    public ResponseEntity<List<StudentPeriodDataDTO>> searchStudentPeriodData(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudentPeriodData for query {}", query);
        Page<StudentPeriodDataDTO> page = studentPeriodDataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/student-period-data");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
