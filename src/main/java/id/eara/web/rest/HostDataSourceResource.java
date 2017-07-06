package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.HostDataSourceService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.HostDataSourceDTO;
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
 * REST controller for managing HostDataSource.
 */
@RestController
@RequestMapping("/api")
public class HostDataSourceResource {

    private final Logger log = LoggerFactory.getLogger(HostDataSourceResource.class);

    private static final String ENTITY_NAME = "hostDataSource";

    private final HostDataSourceService hostDataSourceService;

    public HostDataSourceResource(HostDataSourceService hostDataSourceService) {
        this.hostDataSourceService = hostDataSourceService;
    }

    /**
     * POST  /host-data-sources : Create a new hostDataSource.
     *
     * @param hostDataSourceDTO the hostDataSourceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hostDataSourceDTO, or with status 400 (Bad Request) if the hostDataSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/host-data-sources")
    @Timed
    public ResponseEntity<HostDataSourceDTO> createHostDataSource(@RequestBody HostDataSourceDTO hostDataSourceDTO) throws URISyntaxException {
        log.debug("REST request to save HostDataSource : {}", hostDataSourceDTO);
        if (hostDataSourceDTO.getIdHostDataSource() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new hostDataSource cannot already have an ID")).body(null);
        }
        HostDataSourceDTO result = hostDataSourceService.save(hostDataSourceDTO);
        
        return ResponseEntity.created(new URI("/api/host-data-sources/" + result.getIdHostDataSource()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdHostDataSource().toString()))
            .body(result);
    }

    /**
     * POST  /host-data-sources/execute : Execute Bussiness Process hostDataSource.
     *
     * @param hostDataSourceDTO the hostDataSourceDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  hostDataSourceDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/host-data-sources/execute")
    @Timed
    public ResponseEntity<HostDataSourceDTO> executedHostDataSource(@RequestBody HostDataSourceDTO hostDataSourceDTO) throws URISyntaxException {
        log.debug("REST request to process HostDataSource : {}", hostDataSourceDTO);
        return new ResponseEntity<HostDataSourceDTO>(hostDataSourceDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /host-data-sources : Updates an existing hostDataSource.
     *
     * @param hostDataSourceDTO the hostDataSourceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hostDataSourceDTO,
     * or with status 400 (Bad Request) if the hostDataSourceDTO is not valid,
     * or with status 500 (Internal Server Error) if the hostDataSourceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/host-data-sources")
    @Timed
    public ResponseEntity<HostDataSourceDTO> updateHostDataSource(@RequestBody HostDataSourceDTO hostDataSourceDTO) throws URISyntaxException {
        log.debug("REST request to update HostDataSource : {}", hostDataSourceDTO);
        if (hostDataSourceDTO.getIdHostDataSource() == null) {
            return createHostDataSource(hostDataSourceDTO);
        }
        HostDataSourceDTO result = hostDataSourceService.save(hostDataSourceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hostDataSourceDTO.getIdHostDataSource().toString()))
            .body(result);
    }

    /**
     * GET  /host-data-sources : get all the hostDataSources.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hostDataSources in body
     */
    @GetMapping("/host-data-sources")
    @Timed
    public ResponseEntity<List<HostDataSourceDTO>> getAllHostDataSources(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of HostDataSources");
        Page<HostDataSourceDTO> page = hostDataSourceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/host-data-sources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /host-data-sources/:id : get the "id" hostDataSource.
     *
     * @param id the id of the hostDataSourceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hostDataSourceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/host-data-sources/{id}")
    @Timed
    public ResponseEntity<HostDataSourceDTO> getHostDataSource(@PathVariable UUID id) {
        log.debug("REST request to get HostDataSource : {}", id);
        HostDataSourceDTO hostDataSourceDTO = hostDataSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(hostDataSourceDTO));
    }

    /**
     * DELETE  /host-data-sources/:id : delete the "id" hostDataSource.
     *
     * @param id the id of the hostDataSourceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/host-data-sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteHostDataSource(@PathVariable UUID id) {
        log.debug("REST request to delete HostDataSource : {}", id);
        hostDataSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/host-data-sources?query=:query : search for the hostDataSource corresponding
     * to the query.
     *
     * @param query the query of the hostDataSource search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/host-data-sources")
    @Timed
    public ResponseEntity<List<HostDataSourceDTO>> searchHostDataSources(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of HostDataSources for query {}", query);
        Page<HostDataSourceDTO> page = hostDataSourceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/host-data-sources");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
