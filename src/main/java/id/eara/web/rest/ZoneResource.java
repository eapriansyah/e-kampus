package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ZoneService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ZoneDTO;
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
 * REST controller for managing Zone.
 */
@RestController
@RequestMapping("/api")
public class ZoneResource {

    private final Logger log = LoggerFactory.getLogger(ZoneResource.class);

    private static final String ENTITY_NAME = "zone";

    private final ZoneService zoneService;

    public ZoneResource(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    /**
     * POST  /zones : Create a new zone.
     *
     * @param zoneDTO the zoneDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new zoneDTO, or with status 400 (Bad Request) if the zone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/zones")
    @Timed
    public ResponseEntity<ZoneDTO> createZone(@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
        log.debug("REST request to save Zone : {}", zoneDTO);
        if (zoneDTO.getIdGeoBoundary() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new zone cannot already have an ID")).body(null);
        }
        ZoneDTO result = zoneService.save(zoneDTO);
        
        return ResponseEntity.created(new URI("/api/zones/" + result.getIdGeoBoundary()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdGeoBoundary().toString()))
            .body(result);
    }

    /**
     * POST  /zones/execute : Execute Bussiness Process zone.
     *
     * @param zoneDTO the zoneDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  zoneDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/zones/execute")
    @Timed
    public ResponseEntity<ZoneDTO> executedZone(@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
        log.debug("REST request to process Zone : {}", zoneDTO);
        return new ResponseEntity<ZoneDTO>(zoneDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /zones : Updates an existing zone.
     *
     * @param zoneDTO the zoneDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated zoneDTO,
     * or with status 400 (Bad Request) if the zoneDTO is not valid,
     * or with status 500 (Internal Server Error) if the zoneDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/zones")
    @Timed
    public ResponseEntity<ZoneDTO> updateZone(@RequestBody ZoneDTO zoneDTO) throws URISyntaxException {
        log.debug("REST request to update Zone : {}", zoneDTO);
        if (zoneDTO.getIdGeoBoundary() == null) {
            return createZone(zoneDTO);
        }
        ZoneDTO result = zoneService.save(zoneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, zoneDTO.getIdGeoBoundary().toString()))
            .body(result);
    }

    /**
     * GET  /zones : get all the zones.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of zones in body
     */
    @GetMapping("/zones")
    @Timed
    public ResponseEntity<List<ZoneDTO>> getAllZones(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Zones");
        Page<ZoneDTO> page = zoneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/zones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /zones/:id : get the "id" zone.
     *
     * @param id the id of the zoneDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the zoneDTO, or with status 404 (Not Found)
     */
    @GetMapping("/zones/{id}")
    @Timed
    public ResponseEntity<ZoneDTO> getZone(@PathVariable UUID id) {
        log.debug("REST request to get Zone : {}", id);
        ZoneDTO zoneDTO = zoneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(zoneDTO));
    }

    /**
     * DELETE  /zones/:id : delete the "id" zone.
     *
     * @param id the id of the zoneDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/zones/{id}")
    @Timed
    public ResponseEntity<Void> deleteZone(@PathVariable UUID id) {
        log.debug("REST request to delete Zone : {}", id);
        zoneService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/zones?query=:query : search for the zone corresponding
     * to the query.
     *
     * @param query the query of the zone search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/zones")
    @Timed
    public ResponseEntity<List<ZoneDTO>> searchZones(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Zones for query {}", query);
        Page<ZoneDTO> page = zoneService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/zones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
