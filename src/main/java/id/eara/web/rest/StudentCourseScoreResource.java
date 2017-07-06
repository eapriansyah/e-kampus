package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudentCourseScoreService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudentCourseScoreDTO;
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
 * REST controller for managing StudentCourseScore.
 */
@RestController
@RequestMapping("/api")
public class StudentCourseScoreResource {

    private final Logger log = LoggerFactory.getLogger(StudentCourseScoreResource.class);

    private static final String ENTITY_NAME = "studentCourseScore";

    private final StudentCourseScoreService studentCourseScoreService;

    public StudentCourseScoreResource(StudentCourseScoreService studentCourseScoreService) {
        this.studentCourseScoreService = studentCourseScoreService;
    }

    /**
     * POST  /student-course-scores : Create a new studentCourseScore.
     *
     * @param studentCourseScoreDTO the studentCourseScoreDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentCourseScoreDTO, or with status 400 (Bad Request) if the studentCourseScore has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-course-scores")
    @Timed
    public ResponseEntity<StudentCourseScoreDTO> createStudentCourseScore(@RequestBody StudentCourseScoreDTO studentCourseScoreDTO) throws URISyntaxException {
        log.debug("REST request to save StudentCourseScore : {}", studentCourseScoreDTO);
        if (studentCourseScoreDTO.getIdStudentCourseScore() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studentCourseScore cannot already have an ID")).body(null);
        }
        StudentCourseScoreDTO result = studentCourseScoreService.save(studentCourseScoreDTO);
        
        return ResponseEntity.created(new URI("/api/student-course-scores/" + result.getIdStudentCourseScore()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudentCourseScore().toString()))
            .body(result);
    }

    /**
     * POST  /student-course-scores/execute : Execute Bussiness Process studentCourseScore.
     *
     * @param studentCourseScoreDTO the studentCourseScoreDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studentCourseScoreDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/student-course-scores/execute")
    @Timed
    public ResponseEntity<StudentCourseScoreDTO> executedStudentCourseScore(@RequestBody StudentCourseScoreDTO studentCourseScoreDTO) throws URISyntaxException {
        log.debug("REST request to process StudentCourseScore : {}", studentCourseScoreDTO);
        return new ResponseEntity<StudentCourseScoreDTO>(studentCourseScoreDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /student-course-scores : Updates an existing studentCourseScore.
     *
     * @param studentCourseScoreDTO the studentCourseScoreDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentCourseScoreDTO,
     * or with status 400 (Bad Request) if the studentCourseScoreDTO is not valid,
     * or with status 500 (Internal Server Error) if the studentCourseScoreDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/student-course-scores")
    @Timed
    public ResponseEntity<StudentCourseScoreDTO> updateStudentCourseScore(@RequestBody StudentCourseScoreDTO studentCourseScoreDTO) throws URISyntaxException {
        log.debug("REST request to update StudentCourseScore : {}", studentCourseScoreDTO);
        if (studentCourseScoreDTO.getIdStudentCourseScore() == null) {
            return createStudentCourseScore(studentCourseScoreDTO);
        }
        StudentCourseScoreDTO result = studentCourseScoreService.save(studentCourseScoreDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studentCourseScoreDTO.getIdStudentCourseScore().toString()))
            .body(result);
    }

    /**
     * GET  /student-course-scores : get all the studentCourseScores.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentCourseScores in body
     */
    @GetMapping("/student-course-scores")
    @Timed
    public ResponseEntity<List<StudentCourseScoreDTO>> getAllStudentCourseScores(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentCourseScores");
        Page<StudentCourseScoreDTO> page = studentCourseScoreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-course-scores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/student-course-scores/filterBy")
    @Timed
    public ResponseEntity<List<StudentCourseScoreDTO>> getAllFilteredStudentCourseScores(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudentCourseScores");
        Page<StudentCourseScoreDTO> page = studentCourseScoreService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-course-scores/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /student-course-scores/:id : get the "id" studentCourseScore.
     *
     * @param id the id of the studentCourseScoreDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentCourseScoreDTO, or with status 404 (Not Found)
     */
    @GetMapping("/student-course-scores/{id}")
    @Timed
    public ResponseEntity<StudentCourseScoreDTO> getStudentCourseScore(@PathVariable UUID id) {
        log.debug("REST request to get StudentCourseScore : {}", id);
        StudentCourseScoreDTO studentCourseScoreDTO = studentCourseScoreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentCourseScoreDTO));
    }

    /**
     * DELETE  /student-course-scores/:id : delete the "id" studentCourseScore.
     *
     * @param id the id of the studentCourseScoreDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/student-course-scores/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudentCourseScore(@PathVariable UUID id) {
        log.debug("REST request to delete StudentCourseScore : {}", id);
        studentCourseScoreService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/student-course-scores?query=:query : search for the studentCourseScore corresponding
     * to the query.
     *
     * @param query the query of the studentCourseScore search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/student-course-scores")
    @Timed
    public ResponseEntity<List<StudentCourseScoreDTO>> searchStudentCourseScores(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudentCourseScores for query {}", query);
        Page<StudentCourseScoreDTO> page = studentCourseScoreService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/student-course-scores");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
