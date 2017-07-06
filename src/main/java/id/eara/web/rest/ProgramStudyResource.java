package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ProgramStudyService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ProgramStudyDTO;
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
 * REST controller for managing ProgramStudy.
 */
@RestController
@RequestMapping("/api")
public class ProgramStudyResource {

    private final Logger log = LoggerFactory.getLogger(ProgramStudyResource.class);

    private static final String ENTITY_NAME = "programStudy";

    private final ProgramStudyService programStudyService;

    public ProgramStudyResource(ProgramStudyService programStudyService) {
        this.programStudyService = programStudyService;
    }

    /**
     * POST  /program-studies : Create a new programStudy.
     *
     * @param programStudyDTO the programStudyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new programStudyDTO, or with status 400 (Bad Request) if the programStudy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-studies")
    @Timed
    public ResponseEntity<ProgramStudyDTO> createProgramStudy(@RequestBody ProgramStudyDTO programStudyDTO) throws URISyntaxException {
        log.debug("REST request to save ProgramStudy : {}", programStudyDTO);
        if (programStudyDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new programStudy cannot already have an ID")).body(null);
        }
        ProgramStudyDTO result = programStudyService.save(programStudyDTO);
        
        return ResponseEntity.created(new URI("/api/program-studies/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /program-studies/execute : Execute Bussiness Process programStudy.
     *
     * @param programStudyDTO the programStudyDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  programStudyDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/program-studies/execute")
    @Timed
    public ResponseEntity<ProgramStudyDTO> executedProgramStudy(@RequestBody ProgramStudyDTO programStudyDTO) throws URISyntaxException {
        log.debug("REST request to process ProgramStudy : {}", programStudyDTO);
        return new ResponseEntity<ProgramStudyDTO>(programStudyDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /program-studies : Updates an existing programStudy.
     *
     * @param programStudyDTO the programStudyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated programStudyDTO,
     * or with status 400 (Bad Request) if the programStudyDTO is not valid,
     * or with status 500 (Internal Server Error) if the programStudyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/program-studies")
    @Timed
    public ResponseEntity<ProgramStudyDTO> updateProgramStudy(@RequestBody ProgramStudyDTO programStudyDTO) throws URISyntaxException {
        log.debug("REST request to update ProgramStudy : {}", programStudyDTO);
        if (programStudyDTO.getIdPartyRole() == null) {
            return createProgramStudy(programStudyDTO);
        }
        ProgramStudyDTO result = programStudyService.save(programStudyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, programStudyDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /program-studies : get all the programStudies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of programStudies in body
     */
    @GetMapping("/program-studies")
    @Timed
    public ResponseEntity<List<ProgramStudyDTO>> getAllProgramStudies(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ProgramStudies");
        Page<ProgramStudyDTO> page = programStudyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/program-studies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /program-studies/:id : get the "id" programStudy.
     *
     * @param id the id of the programStudyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the programStudyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/program-studies/{id}")
    @Timed
    public ResponseEntity<ProgramStudyDTO> getProgramStudy(@PathVariable UUID id) {
        log.debug("REST request to get ProgramStudy : {}", id);
        ProgramStudyDTO programStudyDTO = programStudyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(programStudyDTO));
    }

    /**
     * DELETE  /program-studies/:id : delete the "id" programStudy.
     *
     * @param id the id of the programStudyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/program-studies/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgramStudy(@PathVariable UUID id) {
        log.debug("REST request to delete ProgramStudy : {}", id);
        programStudyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/program-studies?query=:query : search for the programStudy corresponding
     * to the query.
     *
     * @param query the query of the programStudy search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/program-studies")
    @Timed
    public ResponseEntity<List<ProgramStudyDTO>> searchProgramStudies(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ProgramStudies for query {}", query);
        Page<ProgramStudyDTO> page = programStudyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/program-studies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
