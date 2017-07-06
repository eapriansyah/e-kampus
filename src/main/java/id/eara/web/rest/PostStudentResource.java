package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.PostStudentService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.PostStudentDTO;
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
 * REST controller for managing PostStudent.
 */
@RestController
@RequestMapping("/api")
public class PostStudentResource {

    private final Logger log = LoggerFactory.getLogger(PostStudentResource.class);

    private static final String ENTITY_NAME = "postStudent";

    private final PostStudentService postStudentService;

    public PostStudentResource(PostStudentService postStudentService) {
        this.postStudentService = postStudentService;
    }

    /**
     * POST  /post-students : Create a new postStudent.
     *
     * @param postStudentDTO the postStudentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new postStudentDTO, or with status 400 (Bad Request) if the postStudent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-students")
    @Timed
    public ResponseEntity<PostStudentDTO> createPostStudent(@RequestBody PostStudentDTO postStudentDTO) throws URISyntaxException {
        log.debug("REST request to save PostStudent : {}", postStudentDTO);
        if (postStudentDTO.getIdPartyRole() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new postStudent cannot already have an ID")).body(null);
        }
        PostStudentDTO result = postStudentService.save(postStudentDTO);
        
        return ResponseEntity.created(new URI("/api/post-students/" + result.getIdPartyRole()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * POST  /post-students/execute : Execute Bussiness Process postStudent.
     *
     * @param postStudentDTO the postStudentDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  postStudentDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/post-students/execute")
    @Timed
    public ResponseEntity<PostStudentDTO> executedPostStudent(@RequestBody PostStudentDTO postStudentDTO) throws URISyntaxException {
        log.debug("REST request to process PostStudent : {}", postStudentDTO);
        return new ResponseEntity<PostStudentDTO>(postStudentDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /post-students : Updates an existing postStudent.
     *
     * @param postStudentDTO the postStudentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated postStudentDTO,
     * or with status 400 (Bad Request) if the postStudentDTO is not valid,
     * or with status 500 (Internal Server Error) if the postStudentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/post-students")
    @Timed
    public ResponseEntity<PostStudentDTO> updatePostStudent(@RequestBody PostStudentDTO postStudentDTO) throws URISyntaxException {
        log.debug("REST request to update PostStudent : {}", postStudentDTO);
        if (postStudentDTO.getIdPartyRole() == null) {
            return createPostStudent(postStudentDTO);
        }
        PostStudentDTO result = postStudentService.save(postStudentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postStudentDTO.getIdPartyRole().toString()))
            .body(result);
    }

    /**
     * GET  /post-students : get all the postStudents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of postStudents in body
     */
    @GetMapping("/post-students")
    @Timed
    public ResponseEntity<List<PostStudentDTO>> getAllPostStudents(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of PostStudents");
        Page<PostStudentDTO> page = postStudentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/post-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /post-students/:id : get the "id" postStudent.
     *
     * @param id the id of the postStudentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the postStudentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/post-students/{id}")
    @Timed
    public ResponseEntity<PostStudentDTO> getPostStudent(@PathVariable UUID id) {
        log.debug("REST request to get PostStudent : {}", id);
        PostStudentDTO postStudentDTO = postStudentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postStudentDTO));
    }

    /**
     * DELETE  /post-students/:id : delete the "id" postStudent.
     *
     * @param id the id of the postStudentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/post-students/{id}")
    @Timed
    public ResponseEntity<Void> deletePostStudent(@PathVariable UUID id) {
        log.debug("REST request to delete PostStudent : {}", id);
        postStudentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/post-students?query=:query : search for the postStudent corresponding
     * to the query.
     *
     * @param query the query of the postStudent search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/post-students")
    @Timed
    public ResponseEntity<List<PostStudentDTO>> searchPostStudents(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of PostStudents for query {}", query);
        Page<PostStudentDTO> page = postStudentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/post-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
