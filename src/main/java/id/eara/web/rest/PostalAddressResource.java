package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.PostalAddressService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PostalAddressDTO;
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
 * REST controller for managing PostalAddress.
 */
@RestController
@RequestMapping("/api")
public class PostalAddressResource {

    private final Logger log = LoggerFactory.getLogger(PostalAddressResource.class);

    private static final String ENTITY_NAME = "postalAddress";

    private final PostalAddressService postalAddressService;

    public PostalAddressResource(PostalAddressService postalAddressService) {
        this.postalAddressService = postalAddressService;
    }

    /**
     * POST  /postal-addresses : Create a new postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postalAddressDTO, or with status 400 (Bad Request) if the postalAddress has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/postal-addresses")
    @Timed
    public ResponseEntity<PostalAddressDTO> createPostalAddress(@RequestBody PostalAddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to save PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getIdContact() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postalAddress cannot already have an ID")).body(null);
        }
        PostalAddressDTO result = postalAddressService.save(postalAddressDTO);
        
        return ResponseEntity.created(new URI("/api/postal-addresses/" + result.getIdContact()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdContact().toString()))
            .body(result);
    }

    /**
     * POST  /postal-addresses/execute : Execute Bussiness Process postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  postalAddressDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/postal-addresses/execute")
    @Timed
    public ResponseEntity<PostalAddressDTO> executedPostalAddress(@RequestBody PostalAddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to process PostalAddress : {}", postalAddressDTO);
        return new ResponseEntity<PostalAddressDTO>(postalAddressDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /postal-addresses : Updates an existing postalAddress.
     *
     * @param postalAddressDTO the postalAddressDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postalAddressDTO,
     * or with status 400 (Bad Request) if the postalAddressDTO is not valid,
     * or with status 500 (Internal Server Error) if the postalAddressDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/postal-addresses")
    @Timed
    public ResponseEntity<PostalAddressDTO> updatePostalAddress(@RequestBody PostalAddressDTO postalAddressDTO) throws URISyntaxException {
        log.debug("REST request to update PostalAddress : {}", postalAddressDTO);
        if (postalAddressDTO.getIdContact() == null) {
            return createPostalAddress(postalAddressDTO);
        }
        PostalAddressDTO result = postalAddressService.save(postalAddressDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postalAddressDTO.getIdContact().toString()))
            .body(result);
    }

    /**
     * GET  /postal-addresses : get all the postalAddresses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of postalAddresses in body
     */
    @GetMapping("/postal-addresses")
    @Timed
    public ResponseEntity<List<PostalAddressDTO>> getAllPostalAddresses(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PostalAddresses");
        Page<PostalAddressDTO> page = postalAddressService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postal-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /postal-addresses/:id : get the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postalAddressDTO, or with status 404 (Not Found)
     */
    @GetMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<PostalAddressDTO> getPostalAddress(@PathVariable UUID id) {
        log.debug("REST request to get PostalAddress : {}", id);
        PostalAddressDTO postalAddressDTO = postalAddressService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postalAddressDTO));
    }

    /**
     * DELETE  /postal-addresses/:id : delete the "id" postalAddress.
     *
     * @param id the id of the postalAddressDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/postal-addresses/{id}")
    @Timed
    public ResponseEntity<Void> deletePostalAddress(@PathVariable UUID id) {
        log.debug("REST request to delete PostalAddress : {}", id);
        postalAddressService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/postal-addresses?query=:query : search for the postalAddress corresponding
     * to the query.
     *
     * @param query the query of the postalAddress search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/postal-addresses")
    @Timed
    public ResponseEntity<List<PostalAddressDTO>> searchPostalAddresses(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PostalAddresses for query {}", query);
        Page<PostalAddressDTO> page = postalAddressService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/postal-addresses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
