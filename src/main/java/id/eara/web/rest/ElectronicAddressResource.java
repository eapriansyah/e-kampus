package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ElectronicAddressService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ElectronicAddressDTO;
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
 * REST controller for managing ElectronicAddress.
 */
@RestController
@RequestMapping("/api")
public class ElectronicAddressResource {

    private final Logger log = LoggerFactory.getLogger(ElectronicAddressResource.class);

    private static final String ENTITY_NAME = "electronicAddress";

    private final ElectronicAddressService electronicAddressService;

    public ElectronicAddressResource(ElectronicAddressService electronicAddressService) {
        this.electronicAddressService = electronicAddressService;
    }

    /**
     * POST  /electronic-addresses : Create a new electronicAddress.
     *
     * @param electronicAddressDTO the electronicAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new electronicAddressDTO, or with status 400 (Bad Request) if the electronicAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/electronic-addresses")
    @Timed
    public ResponseEntity<ElectronicAddressDTO> createElectronicAddress(@RequestBody ElectronicAddressDTO electronicAddressDTO) throws URISyntaxException {
        log.debug("REST request to save ElectronicAddress : {}", electronicAddressDTO);
        if (electronicAddressDTO.getIdContact() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new electronicAddress cannot already have an ID")).body(null);
        }
        ElectronicAddressDTO result = electronicAddressService.save(electronicAddressDTO);
        
        return ResponseEntity.created(new URI("/api/electronic-addresses/" + result.getIdContact()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContact().toString()))
            .body(result);
    }

    /**
     * POST  /electronic-addresses/execute : Execute Bussiness Process electronicAddress.
     *
     * @param electronicAddressDTO the electronicAddressDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  electronicAddressDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/electronic-addresses/execute")
    @Timed
    public ResponseEntity<ElectronicAddressDTO> executedElectronicAddress(@RequestBody ElectronicAddressDTO electronicAddressDTO) throws URISyntaxException {
        log.debug("REST request to process ElectronicAddress : {}", electronicAddressDTO);
        return new ResponseEntity<ElectronicAddressDTO>(electronicAddressDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /electronic-addresses : Updates an existing electronicAddress.
     *
     * @param electronicAddressDTO the electronicAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated electronicAddressDTO,
     * or with status 400 (Bad Request) if the electronicAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the electronicAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/electronic-addresses")
    @Timed
    public ResponseEntity<ElectronicAddressDTO> updateElectronicAddress(@RequestBody ElectronicAddressDTO electronicAddressDTO) throws URISyntaxException {
        log.debug("REST request to update ElectronicAddress : {}", electronicAddressDTO);
        if (electronicAddressDTO.getIdContact() == null) {
            return createElectronicAddress(electronicAddressDTO);
        }
        ElectronicAddressDTO result = electronicAddressService.save(electronicAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, electronicAddressDTO.getIdContact().toString()))
            .body(result);
    }

    /**
     * GET  /electronic-addresses : get all the electronicAddresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of electronicAddresses in body
     */
    @GetMapping("/electronic-addresses")
    @Timed
    public ResponseEntity<List<ElectronicAddressDTO>> getAllElectronicAddresses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ElectronicAddresses");
        Page<ElectronicAddressDTO> page = electronicAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/electronic-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /electronic-addresses/:id : get the "id" electronicAddress.
     *
     * @param id the id of the electronicAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the electronicAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/electronic-addresses/{id}")
    @Timed
    public ResponseEntity<ElectronicAddressDTO> getElectronicAddress(@PathVariable UUID id) {
        log.debug("REST request to get ElectronicAddress : {}", id);
        ElectronicAddressDTO electronicAddressDTO = electronicAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(electronicAddressDTO));
    }

    /**
     * DELETE  /electronic-addresses/:id : delete the "id" electronicAddress.
     *
     * @param id the id of the electronicAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/electronic-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deleteElectronicAddress(@PathVariable UUID id) {
        log.debug("REST request to delete ElectronicAddress : {}", id);
        electronicAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/electronic-addresses?query=:query : search for the electronicAddress corresponding
     * to the query.
     *
     * @param query the query of the electronicAddress search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/electronic-addresses")
    @Timed
    public ResponseEntity<List<ElectronicAddressDTO>> searchElectronicAddresses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ElectronicAddresses for query {}", query);
        Page<ElectronicAddressDTO> page = electronicAddressService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/electronic-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
