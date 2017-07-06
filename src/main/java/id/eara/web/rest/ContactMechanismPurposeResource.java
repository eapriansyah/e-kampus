package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ContactMechanismPurposeService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ContactMechanismPurposeDTO;
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
 * REST controller for managing ContactMechanismPurpose.
 */
@RestController
@RequestMapping("/api")
public class ContactMechanismPurposeResource {

    private final Logger log = LoggerFactory.getLogger(ContactMechanismPurposeResource.class);

    private static final String ENTITY_NAME = "contactMechanismPurpose";

    private final ContactMechanismPurposeService contactMechanismPurposeService;

    public ContactMechanismPurposeResource(ContactMechanismPurposeService contactMechanismPurposeService) {
        this.contactMechanismPurposeService = contactMechanismPurposeService;
    }

    /**
     * POST  /contact-mechanism-purposes : Create a new contactMechanismPurpose.
     *
     * @param contactMechanismPurposeDTO the contactMechanismPurposeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contactMechanismPurposeDTO, or with status 400 (Bad Request) if the contactMechanismPurpose has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-mechanism-purposes")
    @Timed
    public ResponseEntity<ContactMechanismPurposeDTO> createContactMechanismPurpose(@RequestBody ContactMechanismPurposeDTO contactMechanismPurposeDTO) throws URISyntaxException {
        log.debug("REST request to save ContactMechanismPurpose : {}", contactMechanismPurposeDTO);
        if (contactMechanismPurposeDTO.getIdContactMechPurpose() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new contactMechanismPurpose cannot already have an ID")).body(null);
        }
        ContactMechanismPurposeDTO result = contactMechanismPurposeService.save(contactMechanismPurposeDTO);
        
        return ResponseEntity.created(new URI("/api/contact-mechanism-purposes/" + result.getIdContactMechPurpose()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContactMechPurpose().toString()))
            .body(result);
    }

    /**
     * POST  /contact-mechanism-purposes/execute : Execute Bussiness Process contactMechanismPurpose.
     *
     * @param contactMechanismPurposeDTO the contactMechanismPurposeDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  contactMechanismPurposeDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contact-mechanism-purposes/execute")
    @Timed
    public ResponseEntity<ContactMechanismPurposeDTO> executedContactMechanismPurpose(@RequestBody ContactMechanismPurposeDTO contactMechanismPurposeDTO) throws URISyntaxException {
        log.debug("REST request to process ContactMechanismPurpose : {}", contactMechanismPurposeDTO);
        return new ResponseEntity<ContactMechanismPurposeDTO>(contactMechanismPurposeDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /contact-mechanism-purposes : Updates an existing contactMechanismPurpose.
     *
     * @param contactMechanismPurposeDTO the contactMechanismPurposeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contactMechanismPurposeDTO,
     * or with status 400 (Bad Request) if the contactMechanismPurposeDTO is not valid,
     * or with status 500 (Internal Server Error) if the contactMechanismPurposeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contact-mechanism-purposes")
    @Timed
    public ResponseEntity<ContactMechanismPurposeDTO> updateContactMechanismPurpose(@RequestBody ContactMechanismPurposeDTO contactMechanismPurposeDTO) throws URISyntaxException {
        log.debug("REST request to update ContactMechanismPurpose : {}", contactMechanismPurposeDTO);
        if (contactMechanismPurposeDTO.getIdContactMechPurpose() == null) {
            return createContactMechanismPurpose(contactMechanismPurposeDTO);
        }
        ContactMechanismPurposeDTO result = contactMechanismPurposeService.save(contactMechanismPurposeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contactMechanismPurposeDTO.getIdContactMechPurpose().toString()))
            .body(result);
    }

    /**
     * GET  /contact-mechanism-purposes : get all the contactMechanismPurposes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of contactMechanismPurposes in body
     */
    @GetMapping("/contact-mechanism-purposes")
    @Timed
    public ResponseEntity<List<ContactMechanismPurposeDTO>> getAllContactMechanismPurposes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ContactMechanismPurposes");
        Page<ContactMechanismPurposeDTO> page = contactMechanismPurposeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-mechanism-purposes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    @GetMapping("/contact-mechanism-purposes/filterBy")
    @Timed
    public ResponseEntity<List<ContactMechanismPurposeDTO>> getAllFilteredContactMechanismPurposes(@RequestParam String filterBy, @ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ContactMechanismPurposes");
        Page<ContactMechanismPurposeDTO> page = contactMechanismPurposeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contact-mechanism-purposes/filterBy");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    

    /**
     * GET  /contact-mechanism-purposes/:id : get the "id" contactMechanismPurpose.
     *
     * @param id the id of the contactMechanismPurposeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contactMechanismPurposeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/contact-mechanism-purposes/{id}")
    @Timed
    public ResponseEntity<ContactMechanismPurposeDTO> getContactMechanismPurpose(@PathVariable UUID id) {
        log.debug("REST request to get ContactMechanismPurpose : {}", id);
        ContactMechanismPurposeDTO contactMechanismPurposeDTO = contactMechanismPurposeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contactMechanismPurposeDTO));
    }

    /**
     * DELETE  /contact-mechanism-purposes/:id : delete the "id" contactMechanismPurpose.
     *
     * @param id the id of the contactMechanismPurposeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contact-mechanism-purposes/{id}")
    @Timed
    public ResponseEntity<Void> deleteContactMechanismPurpose(@PathVariable UUID id) {
        log.debug("REST request to delete ContactMechanismPurpose : {}", id);
        contactMechanismPurposeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contact-mechanism-purposes?query=:query : search for the contactMechanismPurpose corresponding
     * to the query.
     *
     * @param query the query of the contactMechanismPurpose search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/contact-mechanism-purposes")
    @Timed
    public ResponseEntity<List<ContactMechanismPurposeDTO>> searchContactMechanismPurposes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ContactMechanismPurposes for query {}", query);
        Page<ContactMechanismPurposeDTO> page = contactMechanismPurposeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/contact-mechanism-purposes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
