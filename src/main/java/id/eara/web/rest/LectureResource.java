package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.LectureService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.LectureDTO;
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
 * REST controller for managing Lecture.
 */
@RestController
@RequestMapping("/api")
public class LectureResource {

    private final Logger log = LoggerFactory.getLogger(LectureResource.class);

    private static final String ENTITY_NAME = "lecture";

    private final LectureService lectureService;

    public LectureResource(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    /**
     * POST  /lectures : Create a new lecture.
     *
     * @param lectureDTO the lectureDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lectureDTO, or with status 400 (Bad Request) if the lecture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lectures")
    @Timed
    public ResponseEntity<LectureDTO> createLecture(@RequestBody LectureDTO lectureDTO) throws URISyntaxException {
        log.debug("REST request to save Lecture : {}", lectureDTO);
        if (lectureDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new lecture cannot already have an ID")).body(null);
        }
        LectureDTO result = lectureService.save(lectureDTO);
        
        return ResponseEntity.created(new URI("/api/lectures/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /lectures/execute : Execute Bussiness Process lecture.
     *
     * @param lectureDTO the lectureDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  lectureDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lectures/execute")
    @Timed
    public ResponseEntity<LectureDTO> executedLecture(@RequestBody LectureDTO lectureDTO) throws URISyntaxException {
        log.debug("REST request to process Lecture : {}", lectureDTO);
        return new ResponseEntity<LectureDTO>(lectureDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /lectures : Updates an existing lecture.
     *
     * @param lectureDTO the lectureDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lectureDTO,
     * or with status 400 (Bad Request) if the lectureDTO is not valid,
     * or with status 500 (Internal Server Error) if the lectureDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lectures")
    @Timed
    public ResponseEntity<LectureDTO> updateLecture(@RequestBody LectureDTO lectureDTO) throws URISyntaxException {
        log.debug("REST request to update Lecture : {}", lectureDTO);
        if (lectureDTO.getIdPartyRole() == null) {
            return createLecture(lectureDTO);
        }
        LectureDTO result = lectureService.save(lectureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, lectureDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /lectures : get all the lectures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lectures in body
     */
    @GetMapping("/lectures")
    @Timed
    public ResponseEntity<List<LectureDTO>> getAllLectures(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Lectures");
        Page<LectureDTO> page = lectureService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /lectures/:id : get the "id" lecture.
     *
     * @param id the id of the lectureDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lectureDTO, or with status 404 (Not Found)
     */
    @GetMapping("/lectures/{id}")
    @Timed
    public ResponseEntity<LectureDTO> getLecture(@PathVariable UUID id) {
        log.debug("REST request to get Lecture : {}", id);
        LectureDTO lectureDTO = lectureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(lectureDTO));
    }

    /**
     * DELETE  /lectures/:id : delete the "id" lecture.
     *
     * @param id the id of the lectureDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lectures/{id}")
    @Timed
    public ResponseEntity<Void> deleteLecture(@PathVariable UUID id) {
        log.debug("REST request to delete Lecture : {}", id);
        lectureService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/lectures?query=:query : search for the lecture corresponding
     * to the query.
     *
     * @param query the query of the lecture search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/lectures")
    @Timed
    public ResponseEntity<List<LectureDTO>> searchLectures(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Lectures for query {}", query);
        Page<LectureDTO> page = lectureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lectures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
