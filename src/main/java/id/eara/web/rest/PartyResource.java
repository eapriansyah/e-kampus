package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.PartyService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PartyDTO;
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
 * REST controller for managing Party.
 */
@RestController
@RequestMapping("/api")
public class PartyResource {

    private final Logger log = LoggerFactory.getLogger(PartyResource.class);

    private static final String ENTITY_NAME = "party";

    private final PartyService partyService;

    public PartyResource(PartyService partyService) {
        this.partyService = partyService;
    }

    /**
     * POST  /parties : Create a new party.
     *
     * @param partyDTO the partyDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new partyDTO, or with status 400 (Bad Request) if the party has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parties")
    @Timed
    public ResponseEntity<PartyDTO> createParty(@RequestBody PartyDTO partyDTO) throws URISyntaxException {
        log.debug("REST request to save Party : {}", partyDTO);
        if (partyDTO.getIdParty() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new party cannot already have an ID")).body(null);
        }
        PartyDTO result = partyService.save(partyDTO);
        
        return ResponseEntity.created(new URI("/api/parties/" + result.getIdParty()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdParty().toString()))
            .body(result);
    }

    /**
     * POST  /parties/execute : Execute Bussiness Process party.
     *
     * @param partyDTO the partyDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  partyDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parties/execute")
    @Timed
    public ResponseEntity<PartyDTO> executedParty(@RequestBody PartyDTO partyDTO) throws URISyntaxException {
        log.debug("REST request to process Party : {}", partyDTO);
        return new ResponseEntity<PartyDTO>(partyDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /parties : Updates an existing party.
     *
     * @param partyDTO the partyDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated partyDTO,
     * or with status 400 (Bad Request) if the partyDTO is not valid,
     * or with status 500 (Internal Server Error) if the partyDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parties")
    @Timed
    public ResponseEntity<PartyDTO> updateParty(@RequestBody PartyDTO partyDTO) throws URISyntaxException {
        log.debug("REST request to update Party : {}", partyDTO);
        if (partyDTO.getIdParty() == null) {
            return createParty(partyDTO);
        }
        PartyDTO result = partyService.save(partyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, partyDTO.getIdParty().toString()))
            .body(result);
    }

    /**
     * GET  /parties : get all the parties.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of parties in body
     */
    @GetMapping("/parties")
    @Timed
    public ResponseEntity<List<PartyDTO>> getAllParties(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Parties");
        Page<PartyDTO> page = partyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /parties/:id : get the "id" party.
     *
     * @param id the id of the partyDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the partyDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parties/{id}")
    @Timed
    public ResponseEntity<PartyDTO> getParty(@PathVariable UUID id) {
        log.debug("REST request to get Party : {}", id);
        PartyDTO partyDTO = partyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(partyDTO));
    }

    /**
     * DELETE  /parties/:id : delete the "id" party.
     *
     * @param id the id of the partyDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parties/{id}")
    @Timed
    public ResponseEntity<Void> deleteParty(@PathVariable UUID id) {
        log.debug("REST request to delete Party : {}", id);
        partyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/parties?query=:query : search for the party corresponding
     * to the query.
     *
     * @param query the query of the party search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/parties")
    @Timed
    public ResponseEntity<List<PartyDTO>> searchParties(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Parties for query {}", query);
        Page<PartyDTO> page = partyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/parties");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
