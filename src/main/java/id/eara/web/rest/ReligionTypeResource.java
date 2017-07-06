package id.eara.web.rest;


import com.codahale.metrics.annotation.Timed;
import id.eara.service.ReligionTypeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ReligionTypeDTO;
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
 * REST controller for managing ReligionType.
 */
@RestController
@RequestMapping("/api")
public class ReligionTypeResource {

    private final Logger log = LoggerFactory.getLogger(ReligionTypeResource.class);

    private static final String ENTITY_NAME = "religionType";

    private final ReligionTypeService religionTypeService;

    public ReligionTypeResource(ReligionTypeService religionTypeService) {
        this.religionTypeService = religionTypeService;
    }

    /**
     * POST  /religion-types : Create a new religionType.
     *
     * @param religionTypeDTO the religionTypeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new religionTypeDTO, or with status 400 (Bad Request) if the religionType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/religion-types")
    @Timed
    public ResponseEntity<ReligionTypeDTO> createReligionType(@RequestBody ReligionTypeDTO religionTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ReligionType : {}", religionTypeDTO);
        if (religionTypeDTO.getIdReligionType() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new religionType cannot already have an ID")).body(null);
        }
        ReligionTypeDTO result = religionTypeService.save(religionTypeDTO);
        
        return ResponseEntity.created(new URI("/api/religion-types/" + result.getIdReligionType()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdReligionType().toString()))
            .body(result);
    }

    /**
     * POST  /religion-types/execute : Execute Bussiness Process religionType.
     *
     * @param religionTypeDTO the religionTypeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  religionTypeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/religion-types/execute")
    @Timed
    public ResponseEntity<ReligionTypeDTO> executedReligionType(@RequestBody ReligionTypeDTO religionTypeDTO) throws URISyntaxException {
        log.debug("REST request to process ReligionType : {}", religionTypeDTO);
        return new ResponseEntity<ReligionTypeDTO>(religionTypeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /religion-types : Updates an existing religionType.
     *
     * @param religionTypeDTO the religionTypeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated religionTypeDTO,
     * or with status 400 (Bad Request) if the religionTypeDTO is not valid,
     * or with status 500 (Internal Server Error) if the religionTypeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/religion-types")
    @Timed
    public ResponseEntity<ReligionTypeDTO> updateReligionType(@RequestBody ReligionTypeDTO religionTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ReligionType : {}", religionTypeDTO);
        if (religionTypeDTO.getIdReligionType() == null) {
            return createReligionType(religionTypeDTO);
        }
        ReligionTypeDTO result = religionTypeService.save(religionTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, religionTypeDTO.getIdReligionType().toString()))
            .body(result);
    }

    /**
     * GET  /religion-types : get all the religionTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of religionTypes in body
     */
    @GetMapping("/religion-types")
    @Timed
    public ResponseEntity<List<ReligionTypeDTO>> getAllReligionTypes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ReligionTypes");
        Page<ReligionTypeDTO> page = religionTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/religion-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /religion-types/:id : get the "id" religionType.
     *
     * @param id the id of the religionTypeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the religionTypeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/religion-types/{id}")
    @Timed
    public ResponseEntity<ReligionTypeDTO> getReligionType(@PathVariable Long id) {
        log.debug("REST request to get ReligionType : {}", id);
        ReligionTypeDTO religionTypeDTO = religionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(religionTypeDTO));
    }

    /**
     * DELETE  /religion-types/:id : delete the "id" religionType.
     *
     * @param id the id of the religionTypeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/religion-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteReligionType(@PathVariable Long id) {
        log.debug("REST request to delete ReligionType : {}", id);
        religionTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/religion-types?query=:query : search for the religionType corresponding
     * to the query.
     *
     * @param query the query of the religionType search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/religion-types")
    @Timed
    public ResponseEntity<List<ReligionTypeDTO>> searchReligionTypes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ReligionTypes for query {}", query);
        Page<ReligionTypeDTO> page = religionTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/religion-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
