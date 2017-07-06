package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.PeriodTypeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PeriodTypeDTO;
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
 * REST controller for managing PeriodType.
 */
@RestController
@RequestMapping("/api")
public class PeriodTypeResource {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeResource.class);

    private static final String ENTITY_NAME = "periodType";

    private final PeriodTypeService periodTypeService;

    public PeriodTypeResource(PeriodTypeService periodTypeService) {
        this.periodTypeService = periodTypeService;
    }

    /**
     * POST  /period-types : Create a new periodType.
     *
     * @param periodTypeDTO the periodTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new periodTypeDTO, or with status 400 (Bad Request) if the periodType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/period-types")
    @Timed
    public ResponseEntity<PeriodTypeDTO> createPeriodType(@RequestBody PeriodTypeDTO periodTypeDTO) throws URISyntaxException {
        log.debug("REST request to save PeriodType : {}", periodTypeDTO);
        if (periodTypeDTO.getIdPeriodType() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new periodType cannot already have an ID")).body(null);
        }
        PeriodTypeDTO result = periodTypeService.save(periodTypeDTO);
        
        return ResponseEntity.created(new URI("/api/period-types/" + result.getIdPeriodType()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPeriodType().toString()))
            .body(result);
    }

    /**
     * POST  /period-types/execute : Execute Bussiness Process periodType.
     *
     * @param periodTypeDTO the periodTypeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  periodTypeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/period-types/execute")
    @Timed
    public ResponseEntity<PeriodTypeDTO> executedPeriodType(@RequestBody PeriodTypeDTO periodTypeDTO) throws URISyntaxException {
        log.debug("REST request to process PeriodType : {}", periodTypeDTO);
        return new ResponseEntity<PeriodTypeDTO>(periodTypeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /period-types : Updates an existing periodType.
     *
     * @param periodTypeDTO the periodTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated periodTypeDTO,
     * or with status 400 (Bad Request) if the periodTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the periodTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/period-types")
    @Timed
    public ResponseEntity<PeriodTypeDTO> updatePeriodType(@RequestBody PeriodTypeDTO periodTypeDTO) throws URISyntaxException {
        log.debug("REST request to update PeriodType : {}", periodTypeDTO);
        if (periodTypeDTO.getIdPeriodType() == null) {
            return createPeriodType(periodTypeDTO);
        }
        PeriodTypeDTO result = periodTypeService.save(periodTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, periodTypeDTO.getIdPeriodType().toString()))
            .body(result);
    }

    /**
     * GET  /period-types : get all the periodTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of periodTypes in body
     */
    @GetMapping("/period-types")
    @Timed
    public ResponseEntity<List<PeriodTypeDTO>> getAllPeriodTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PeriodTypes");
        Page<PeriodTypeDTO> page = periodTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/period-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /period-types/:id : get the "id" periodType.
     *
     * @param id the id of the periodTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the periodTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/period-types/{id}")
    @Timed
    public ResponseEntity<PeriodTypeDTO> getPeriodType(@PathVariable Long id) {
        log.debug("REST request to get PeriodType : {}", id);
        PeriodTypeDTO periodTypeDTO = periodTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(periodTypeDTO));
    }

    /**
     * DELETE  /period-types/:id : delete the "id" periodType.
     *
     * @param id the id of the periodTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/period-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePeriodType(@PathVariable Long id) {
        log.debug("REST request to delete PeriodType : {}", id);
        periodTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/period-types?query=:query : search for the periodType corresponding
     * to the query.
     *
     * @param query the query of the periodType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/period-types")
    @Timed
    public ResponseEntity<List<PeriodTypeDTO>> searchPeriodTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PeriodTypes for query {}", query);
        Page<PeriodTypeDTO> page = periodTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/period-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
