package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.AcademicPeriodsService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.AcademicPeriodsDTO;
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
 * REST controller for managing AcademicPeriods.
 */
@RestController
@RequestMapping("/api")
public class AcademicPeriodsResource {

    private final Logger log = LoggerFactory.getLogger(AcademicPeriodsResource.class);

    private static final String ENTITY_NAME = "academicPeriods";

    private final AcademicPeriodsService academicPeriodsService;

    public AcademicPeriodsResource(AcademicPeriodsService academicPeriodsService) {
        this.academicPeriodsService = academicPeriodsService;
    }

    /**
     * POST  /academic-periods : Create a new academicPeriods.
     *
     * @param academicPeriodsDTO the academicPeriodsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new academicPeriodsDTO, or with status 400 (Bad Request) if the academicPeriods has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/academic-periods")
    @Timed
    public ResponseEntity<AcademicPeriodsDTO> createAcademicPeriods(@RequestBody AcademicPeriodsDTO academicPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to save AcademicPeriods : {}", academicPeriodsDTO);
        if (academicPeriodsDTO.getIdPeriod() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new academicPeriods cannot already have an ID")).body(null);
        }
        AcademicPeriodsDTO result = academicPeriodsService.save(academicPeriodsDTO);
        
        return ResponseEntity.created(new URI("/api/academic-periods/" + result.getIdPeriod()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPeriod().toString()))
            .body(result);
    }

    /**
     * POST  /academic-periods/execute : Execute Bussiness Process academicPeriods.
     *
     * @param academicPeriodsDTO the academicPeriodsDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  academicPeriodsDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/academic-periods/execute")
    @Timed
    public ResponseEntity<AcademicPeriodsDTO> executedAcademicPeriods(@RequestBody AcademicPeriodsDTO academicPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to process AcademicPeriods : {}", academicPeriodsDTO);
        return new ResponseEntity<AcademicPeriodsDTO>(academicPeriodsDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /academic-periods : Updates an existing academicPeriods.
     *
     * @param academicPeriodsDTO the academicPeriodsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated academicPeriodsDTO,
     * or with status 400 (Bad Request) if the academicPeriodsDTO is not valid,
     * or with status 500 (Internal Server Error) if the academicPeriodsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/academic-periods")
    @Timed
    public ResponseEntity<AcademicPeriodsDTO> updateAcademicPeriods(@RequestBody AcademicPeriodsDTO academicPeriodsDTO) throws URISyntaxException {
        log.debug("REST request to update AcademicPeriods : {}", academicPeriodsDTO);
        if (academicPeriodsDTO.getIdPeriod() == null) {
            return createAcademicPeriods(academicPeriodsDTO);
        }
        AcademicPeriodsDTO result = academicPeriodsService.save(academicPeriodsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, academicPeriodsDTO.getIdPeriod().toString()))
            .body(result);
    }

    /**
     * GET  /academic-periods : get all the academicPeriods.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of academicPeriods in body
     */
    @GetMapping("/academic-periods")
    @Timed
    public ResponseEntity<List<AcademicPeriodsDTO>> getAllAcademicPeriods(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AcademicPeriods");
        Page<AcademicPeriodsDTO> page = academicPeriodsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/academic-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/academic-periods/filterBy")
    @Timed
    public ResponseEntity<List<AcademicPeriodsDTO>> getAllFilteredAcademicPeriods(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AcademicPeriods");
        Page<AcademicPeriodsDTO> page = academicPeriodsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/academic-periods/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /academic-periods/:id : get the "id" academicPeriods.
     *
     * @param id the id of the academicPeriodsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the academicPeriodsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/academic-periods/{id}")
    @Timed
    public ResponseEntity<AcademicPeriodsDTO> getAcademicPeriods(@PathVariable UUID id) {
        log.debug("REST request to get AcademicPeriods : {}", id);
        AcademicPeriodsDTO academicPeriodsDTO = academicPeriodsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(academicPeriodsDTO));
    }

    /**
     * DELETE  /academic-periods/:id : delete the "id" academicPeriods.
     *
     * @param id the id of the academicPeriodsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/academic-periods/{id}")
    @Timed
    public ResponseEntity<Void> deleteAcademicPeriods(@PathVariable UUID id) {
        log.debug("REST request to delete AcademicPeriods : {}", id);
        academicPeriodsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/academic-periods?query=:query : search for the academicPeriods corresponding
     * to the query.
     *
     * @param query the query of the academicPeriods search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/academic-periods")
    @Timed
    public ResponseEntity<List<AcademicPeriodsDTO>> searchAcademicPeriods(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of AcademicPeriods for query {}", query);
        Page<AcademicPeriodsDTO> page = academicPeriodsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/academic-periods");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
