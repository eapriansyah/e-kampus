package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.BuildingService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.BuildingDTO;
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
 * REST controller for managing Building.
 */
@RestController
@RequestMapping("/api")
public class BuildingResource {

    private final Logger log = LoggerFactory.getLogger(BuildingResource.class);

    private static final String ENTITY_NAME = "building";

    private final BuildingService buildingService;

    public BuildingResource(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * POST  /buildings : Create a new building.
     *
     * @param buildingDTO the buildingDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new buildingDTO, or with status 400 (Bad Request) if the building has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buildings")
    @Timed
    public ResponseEntity<BuildingDTO> createBuilding(@RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to save Building : {}", buildingDTO);
        if (buildingDTO.getIdFacility() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new building cannot already have an ID")).body(null);
        }
        BuildingDTO result = buildingService.save(buildingDTO);
        
        return ResponseEntity.created(new URI("/api/buildings/" + result.getIdFacility()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdFacility().toString()))
            .body(result);
    }

    /**
     * POST  /buildings/execute : Execute Bussiness Process building.
     *
     * @param buildingDTO the buildingDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  buildingDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/buildings/execute")
    @Timed
    public ResponseEntity<BuildingDTO> executedBuilding(@RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to process Building : {}", buildingDTO);
        return new ResponseEntity<BuildingDTO>(buildingDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /buildings : Updates an existing building.
     *
     * @param buildingDTO the buildingDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated buildingDTO,
     * or with status 400 (Bad Request) if the buildingDTO is not valid,
     * or with status 500 (Internal Server Error) if the buildingDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/buildings")
    @Timed
    public ResponseEntity<BuildingDTO> updateBuilding(@RequestBody BuildingDTO buildingDTO) throws URISyntaxException {
        log.debug("REST request to update Building : {}", buildingDTO);
        if (buildingDTO.getIdFacility() == null) {
            return createBuilding(buildingDTO);
        }
        BuildingDTO result = buildingService.save(buildingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, buildingDTO.getIdFacility().toString()))
            .body(result);
    }

    /**
     * GET  /buildings : get all the buildings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of buildings in body
     */
    @GetMapping("/buildings")
    @Timed
    public ResponseEntity<List<BuildingDTO>> getAllBuildings(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Buildings");
        Page<BuildingDTO> page = buildingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/buildings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /buildings/:id : get the "id" building.
     *
     * @param id the id of the buildingDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the buildingDTO, or with status 404 (Not Found)
     */
    @GetMapping("/buildings/{id}")
    @Timed
    public ResponseEntity<BuildingDTO> getBuilding(@PathVariable UUID id) {
        log.debug("REST request to get Building : {}", id);
        BuildingDTO buildingDTO = buildingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(buildingDTO));
    }

    /**
     * DELETE  /buildings/:id : delete the "id" building.
     *
     * @param id the id of the buildingDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/buildings/{id}")
    @Timed
    public ResponseEntity<Void> deleteBuilding(@PathVariable UUID id) {
        log.debug("REST request to delete Building : {}", id);
        buildingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/buildings?query=:query : search for the building corresponding
     * to the query.
     *
     * @param query the query of the building search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/buildings")
    @Timed
    public ResponseEntity<List<BuildingDTO>> searchBuildings(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Buildings for query {}", query);
        Page<BuildingDTO> page = buildingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/buildings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
