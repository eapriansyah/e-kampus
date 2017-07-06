package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.StudyPathService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.StudyPathDTO;
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
 * REST controller for managing StudyPath.
 */
@RestController
@RequestMapping("/api")
public class StudyPathResource {

    private final Logger log = LoggerFactory.getLogger(StudyPathResource.class);

    private static final String ENTITY_NAME = "studyPath";

    private final StudyPathService studyPathService;

    public StudyPathResource(StudyPathService studyPathService) {
        this.studyPathService = studyPathService;
    }

    /**
     * POST  /study-paths : Create a new studyPath.
     *
     * @param studyPathDTO the studyPathDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studyPathDTO, or with status 400 (Bad Request) if the studyPath has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/study-paths")
    @Timed
    public ResponseEntity<StudyPathDTO> createStudyPath(@RequestBody StudyPathDTO studyPathDTO) throws URISyntaxException {
        log.debug("REST request to save StudyPath : {}", studyPathDTO);
        if (studyPathDTO.getIdStudyPath() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new studyPath cannot already have an ID")).body(null);
        }
        StudyPathDTO result = studyPathService.save(studyPathDTO);
        
        return ResponseEntity.created(new URI("/api/study-paths/" + result.getIdStudyPath()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdStudyPath().toString()))
            .body(result);
    }

    /**
     * POST  /study-paths/execute : Execute Bussiness Process studyPath.
     *
     * @param studyPathDTO the studyPathDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  studyPathDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/study-paths/execute")
    @Timed
    public ResponseEntity<StudyPathDTO> executedStudyPath(@RequestBody StudyPathDTO studyPathDTO) throws URISyntaxException {
        log.debug("REST request to process StudyPath : {}", studyPathDTO);
        return new ResponseEntity<StudyPathDTO>(studyPathDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /study-paths : Updates an existing studyPath.
     *
     * @param studyPathDTO the studyPathDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studyPathDTO,
     * or with status 400 (Bad Request) if the studyPathDTO is not valid,
     * or with status 500 (Internal Server Error) if the studyPathDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/study-paths")
    @Timed
    public ResponseEntity<StudyPathDTO> updateStudyPath(@RequestBody StudyPathDTO studyPathDTO) throws URISyntaxException {
        log.debug("REST request to update StudyPath : {}", studyPathDTO);
        if (studyPathDTO.getIdStudyPath() == null) {
            return createStudyPath(studyPathDTO);
        }
        StudyPathDTO result = studyPathService.save(studyPathDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, studyPathDTO.getIdStudyPath().toString()))
            .body(result);
    }

    /**
     * GET  /study-paths : get all the studyPaths.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studyPaths in body
     */
    @GetMapping("/study-paths")
    @Timed
    public ResponseEntity<List<StudyPathDTO>> getAllStudyPaths(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of StudyPaths");
        Page<StudyPathDTO> page = studyPathService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/study-paths");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /study-paths/:id : get the "id" studyPath.
     *
     * @param id the id of the studyPathDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studyPathDTO, or with status 404 (Not Found)
     */
    @GetMapping("/study-paths/{id}")
    @Timed
    public ResponseEntity<StudyPathDTO> getStudyPath(@PathVariable Long id) {
        log.debug("REST request to get StudyPath : {}", id);
        StudyPathDTO studyPathDTO = studyPathService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studyPathDTO));
    }

    /**
     * DELETE  /study-paths/:id : delete the "id" studyPath.
     *
     * @param id the id of the studyPathDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/study-paths/{id}")
    @Timed
    public ResponseEntity<Void> deleteStudyPath(@PathVariable Long id) {
        log.debug("REST request to delete StudyPath : {}", id);
        studyPathService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/study-paths?query=:query : search for the studyPath corresponding
     * to the query.
     *
     * @param query the query of the studyPath search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/study-paths")
    @Timed
    public ResponseEntity<List<StudyPathDTO>> searchStudyPaths(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of StudyPaths for query {}", query);
        Page<StudyPathDTO> page = studyPathService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/study-paths");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
