package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.PurposeTypeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PurposeTypeDTO;
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
 * REST controller for managing PurposeType.
 */
@RestController
@RequestMapping("/api")
public class PurposeTypeResource {

    private final Logger log = LoggerFactory.getLogger(PurposeTypeResource.class);

    private static final String ENTITY_NAME = "purposeType";

    private final PurposeTypeService purposeTypeService;

    public PurposeTypeResource(PurposeTypeService purposeTypeService) {
        this.purposeTypeService = purposeTypeService;
    }

    /**
     * POST  /purpose-types : Create a new purposeType.
     *
     * @param purposeTypeDTO the purposeTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new purposeTypeDTO, or with status 400 (Bad Request) if the purposeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purpose-types")
    @Timed
    public ResponseEntity<PurposeTypeDTO> createPurposeType(@RequestBody PurposeTypeDTO purposeTypeDTO) throws URISyntaxException {
        log.debug("REST request to save PurposeType : {}", purposeTypeDTO);
        if (purposeTypeDTO.getIdPurposeType() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new purposeType cannot already have an ID")).body(null);
        }
        PurposeTypeDTO result = purposeTypeService.save(purposeTypeDTO);
        
        return ResponseEntity.created(new URI("/api/purpose-types/" + result.getIdPurposeType()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPurposeType().toString()))
            .body(result);
    }

    /**
     * POST  /purpose-types/execute : Execute Bussiness Process purposeType.
     *
     * @param purposeTypeDTO the purposeTypeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  purposeTypeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/purpose-types/execute")
    @Timed
    public ResponseEntity<PurposeTypeDTO> executedPurposeType(@RequestBody PurposeTypeDTO purposeTypeDTO) throws URISyntaxException {
        log.debug("REST request to process PurposeType : {}", purposeTypeDTO);
        return new ResponseEntity<PurposeTypeDTO>(purposeTypeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /purpose-types : Updates an existing purposeType.
     *
     * @param purposeTypeDTO the purposeTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated purposeTypeDTO,
     * or with status 400 (Bad Request) if the purposeTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the purposeTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/purpose-types")
    @Timed
    public ResponseEntity<PurposeTypeDTO> updatePurposeType(@RequestBody PurposeTypeDTO purposeTypeDTO) throws URISyntaxException {
        log.debug("REST request to update PurposeType : {}", purposeTypeDTO);
        if (purposeTypeDTO.getIdPurposeType() == null) {
            return createPurposeType(purposeTypeDTO);
        }
        PurposeTypeDTO result = purposeTypeService.save(purposeTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, purposeTypeDTO.getIdPurposeType().toString()))
            .body(result);
    }

    /**
     * GET  /purpose-types : get all the purposeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of purposeTypes in body
     */
    @GetMapping("/purpose-types")
    @Timed
    public ResponseEntity<List<PurposeTypeDTO>> getAllPurposeTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PurposeTypes");
        Page<PurposeTypeDTO> page = purposeTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/purpose-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /purpose-types/:id : get the "id" purposeType.
     *
     * @param id the id of the purposeTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the purposeTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/purpose-types/{id}")
    @Timed
    public ResponseEntity<PurposeTypeDTO> getPurposeType(@PathVariable Long id) {
        log.debug("REST request to get PurposeType : {}", id);
        PurposeTypeDTO purposeTypeDTO = purposeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(purposeTypeDTO));
    }

    /**
     * DELETE  /purpose-types/:id : delete the "id" purposeType.
     *
     * @param id the id of the purposeTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/purpose-types/{id}")
    @Timed
    public ResponseEntity<Void> deletePurposeType(@PathVariable Long id) {
        log.debug("REST request to delete PurposeType : {}", id);
        purposeTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/purpose-types?query=:query : search for the purposeType corresponding
     * to the query.
     *
     * @param query the query of the purposeType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/purpose-types")
    @Timed
    public ResponseEntity<List<PurposeTypeDTO>> searchPurposeTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PurposeTypes for query {}", query);
        Page<PurposeTypeDTO> page = purposeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/purpose-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
