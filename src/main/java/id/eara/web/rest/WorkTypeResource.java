package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.WorkTypeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.WorkTypeDTO;
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
 * REST controller for managing WorkType.
 */
@RestController
@RequestMapping("/api")
public class WorkTypeResource {

    private final Logger log = LoggerFactory.getLogger(WorkTypeResource.class);

    private static final String ENTITY_NAME = "workType";

    private final WorkTypeService workTypeService;

    public WorkTypeResource(WorkTypeService workTypeService) {
        this.workTypeService = workTypeService;
    }

    /**
     * POST  /work-types : Create a new workType.
     *
     * @param workTypeDTO the workTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workTypeDTO, or with status 400 (Bad Request) if the workType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-types")
    @Timed
    public ResponseEntity<WorkTypeDTO> createWorkType(@RequestBody WorkTypeDTO workTypeDTO) throws URISyntaxException {
        log.debug("REST request to save WorkType : {}", workTypeDTO);
        if (workTypeDTO.getIdWorkType() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new workType cannot already have an ID")).body(null);
        }
        WorkTypeDTO result = workTypeService.save(workTypeDTO);
        
        return ResponseEntity.created(new URI("/api/work-types/" + result.getIdWorkType()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdWorkType().toString()))
            .body(result);
    }

    /**
     * POST  /work-types/execute : Execute Bussiness Process workType.
     *
     * @param workTypeDTO the workTypeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  workTypeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-types/execute")
    @Timed
    public ResponseEntity<WorkTypeDTO> executedWorkType(@RequestBody WorkTypeDTO workTypeDTO) throws URISyntaxException {
        log.debug("REST request to process WorkType : {}", workTypeDTO);
        return new ResponseEntity<WorkTypeDTO>(workTypeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /work-types : Updates an existing workType.
     *
     * @param workTypeDTO the workTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workTypeDTO,
     * or with status 400 (Bad Request) if the workTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the workTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-types")
    @Timed
    public ResponseEntity<WorkTypeDTO> updateWorkType(@RequestBody WorkTypeDTO workTypeDTO) throws URISyntaxException {
        log.debug("REST request to update WorkType : {}", workTypeDTO);
        if (workTypeDTO.getIdWorkType() == null) {
            return createWorkType(workTypeDTO);
        }
        WorkTypeDTO result = workTypeService.save(workTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workTypeDTO.getIdWorkType().toString()))
            .body(result);
    }

    /**
     * GET  /work-types : get all the workTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of workTypes in body
     */
    @GetMapping("/work-types")
    @Timed
    public ResponseEntity<List<WorkTypeDTO>> getAllWorkTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of WorkTypes");
        Page<WorkTypeDTO> page = workTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/work-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /work-types/:id : get the "id" workType.
     *
     * @param id the id of the workTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<WorkTypeDTO> getWorkType(@PathVariable Long id) {
        log.debug("REST request to get WorkType : {}", id);
        WorkTypeDTO workTypeDTO = workTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(workTypeDTO));
    }

    /**
     * DELETE  /work-types/:id : delete the "id" workType.
     *
     * @param id the id of the workTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkType(@PathVariable Long id) {
        log.debug("REST request to delete WorkType : {}", id);
        workTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/work-types?query=:query : search for the workType corresponding
     * to the query.
     *
     * @param query the query of the workType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/work-types")
    @Timed
    public ResponseEntity<List<WorkTypeDTO>> searchWorkTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of WorkTypes for query {}", query);
        Page<WorkTypeDTO> page = workTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/work-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
