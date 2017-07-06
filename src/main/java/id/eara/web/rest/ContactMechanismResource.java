package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ContactMechanismService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ContactMechanismDTO;
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
 * REST controller for managing ContactMechanism.
 */
@RestController
@RequestMapping("/api")
public class ContactMechanismResource {

    private final Logger log = LoggerFactory.getLogger(ContactMechanismResource.class);

    private static final String ENTITY_NAME = "contactMechanism";

    private final ContactMechanismService contactMechanismService;

    public ContactMechanismResource(ContactMechanismService contactMechanismService) {
        this.contactMechanismService = contactMechanismService;
    }

    /**
     * POST  /contact-mechanisms : Create a new contactMechanism.
     *
     * @param contactMechanismDTO the contactMechanismDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactMechanismDTO, or with status 400 (Bad Request) if the contactMechanism has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-mechanisms")
    @Timed
    public ResponseEntity<ContactMechanismDTO> createContactMechanism(@RequestBody ContactMechanismDTO contactMechanismDTO) throws URISyntaxException {
        log.debug("REST request to save ContactMechanism : {}", contactMechanismDTO);
        if (contactMechanismDTO.getIdContact() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new contactMechanism cannot already have an ID")).body(null);
        }
        ContactMechanismDTO result = contactMechanismService.save(contactMechanismDTO);
        
        return ResponseEntity.created(new URI("/api/contact-mechanisms/" + result.getIdContact()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContact().toString()))
            .body(result);
    }

    /**
     * POST  /contact-mechanisms/execute : Execute Bussiness Process contactMechanism.
     *
     * @param contactMechanismDTO the contactMechanismDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  contactMechanismDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-mechanisms/execute")
    @Timed
    public ResponseEntity<ContactMechanismDTO> executedContactMechanism(@RequestBody ContactMechanismDTO contactMechanismDTO) throws URISyntaxException {
        log.debug("REST request to process ContactMechanism : {}", contactMechanismDTO);
        return new ResponseEntity<ContactMechanismDTO>(contactMechanismDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /contact-mechanisms : Updates an existing contactMechanism.
     *
     * @param contactMechanismDTO the contactMechanismDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactMechanismDTO,
     * or with status 400 (Bad Request) if the contactMechanismDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactMechanismDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-mechanisms")
    @Timed
    public ResponseEntity<ContactMechanismDTO> updateContactMechanism(@RequestBody ContactMechanismDTO contactMechanismDTO) throws URISyntaxException {
        log.debug("REST request to update ContactMechanism : {}", contactMechanismDTO);
        if (contactMechanismDTO.getIdContact() == null) {
            return createContactMechanism(contactMechanismDTO);
        }
        ContactMechanismDTO result = contactMechanismService.save(contactMechanismDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactMechanismDTO.getIdContact().toString()))
            .body(result);
    }

    /**
     * GET  /contact-mechanisms : get all the contactMechanisms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactMechanisms in body
     */
    @GetMapping("/contact-mechanisms")
    @Timed
    public ResponseEntity<List<ContactMechanismDTO>> getAllContactMechanisms(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ContactMechanisms");
        Page<ContactMechanismDTO> page = contactMechanismService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-mechanisms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /contact-mechanisms/:id : get the "id" contactMechanism.
     *
     * @param id the id of the contactMechanismDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactMechanismDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contact-mechanisms/{id}")
    @Timed
    public ResponseEntity<ContactMechanismDTO> getContactMechanism(@PathVariable UUID id) {
        log.debug("REST request to get ContactMechanism : {}", id);
        ContactMechanismDTO contactMechanismDTO = contactMechanismService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contactMechanismDTO));
    }

    /**
     * DELETE  /contact-mechanisms/:id : delete the "id" contactMechanism.
     *
     * @param id the id of the contactMechanismDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-mechanisms/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactMechanism(@PathVariable UUID id) {
        log.debug("REST request to delete ContactMechanism : {}", id);
        contactMechanismService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-mechanisms?query=:query : search for the contactMechanism corresponding
     * to the query.
     *
     * @param query the query of the contactMechanism search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contact-mechanisms")
    @Timed
    public ResponseEntity<List<ContactMechanismDTO>> searchContactMechanisms(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ContactMechanisms for query {}", query);
        Page<ContactMechanismDTO> page = contactMechanismService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-mechanisms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
