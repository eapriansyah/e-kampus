package id.eara.web.rest;


import java.util.UUID;

import com.codahale.metrics.annotation.Timed;
import id.eara.service.ClassRoomService;
import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import id.eara.service.dto.ClassRoomDTO;
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
 * REST controller for managing ClassRoom.
 */
@RestController
@RequestMapping("/api")
public class ClassRoomResource {

    private final Logger log = LoggerFactory.getLogger(ClassRoomResource.class);

    private static final String ENTITY_NAME = "classRoom";

    private final ClassRoomService classRoomService;

    public ClassRoomResource(ClassRoomService classRoomService) {
        this.classRoomService = classRoomService;
    }

    /**
     * POST  /class-rooms : Create a new classRoom.
     *
     * @param classRoomDTO the classRoomDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new classRoomDTO, or with status 400 (Bad Request) if the classRoom has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-rooms")
    @Timed
    public ResponseEntity<ClassRoomDTO> createClassRoom(@RequestBody ClassRoomDTO classRoomDTO) throws URISyntaxException {
        log.debug("REST request to save ClassRoom : {}", classRoomDTO);
        if (classRoomDTO.getIdFacility() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new classRoom cannot already have an ID")).body(null);
        }
        ClassRoomDTO result = classRoomService.save(classRoomDTO);
        
        return ResponseEntity.created(new URI("/api/class-rooms/" + result.getIdFacility()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getIdFacility().toString()))
            .body(result);
    }

    /**
     * POST  /class-rooms/execute : Execute Bussiness Process classRoom.
     *
     * @param classRoomDTO the classRoomDTO to Execute
     * @return the ResponseEntity with status Accepted and with body the  classRoomDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/class-rooms/execute")
    @Timed
    public ResponseEntity<ClassRoomDTO> executedClassRoom(@RequestBody ClassRoomDTO classRoomDTO) throws URISyntaxException {
        log.debug("REST request to process ClassRoom : {}", classRoomDTO);
        return new ResponseEntity<ClassRoomDTO>(classRoomDTO, null, HttpStatus.ACCEPTED);
    }

    /**
     * PUT  /class-rooms : Updates an existing classRoom.
     *
     * @param classRoomDTO the classRoomDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated classRoomDTO,
     * or with status 400 (Bad Request) if the classRoomDTO is not valid,
     * or with status 500 (Internal Server Error) if the classRoomDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/class-rooms")
    @Timed
    public ResponseEntity<ClassRoomDTO> updateClassRoom(@RequestBody ClassRoomDTO classRoomDTO) throws URISyntaxException {
        log.debug("REST request to update ClassRoom : {}", classRoomDTO);
        if (classRoomDTO.getIdFacility() == null) {
            return createClassRoom(classRoomDTO);
        }
        ClassRoomDTO result = classRoomService.save(classRoomDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, classRoomDTO.getIdFacility().toString()))
            .body(result);
    }

    /**
     * GET  /class-rooms : get all the classRooms.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of classRooms in body
     */
    @GetMapping("/class-rooms")
    @Timed
    public ResponseEntity<List<ClassRoomDTO>> getAllClassRooms(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ClassRooms");
        Page<ClassRoomDTO> page = classRoomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/class-rooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    

    /**
     * GET  /class-rooms/:id : get the "id" classRoom.
     *
     * @param id the id of the classRoomDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the classRoomDTO, or with status 404 (Not Found)
     */
    @GetMapping("/class-rooms/{id}")
    @Timed
    public ResponseEntity<ClassRoomDTO> getClassRoom(@PathVariable UUID id) {
        log.debug("REST request to get ClassRoom : {}", id);
        ClassRoomDTO classRoomDTO = classRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(classRoomDTO));
    }

    /**
     * DELETE  /class-rooms/:id : delete the "id" classRoom.
     *
     * @param id the id of the classRoomDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/class-rooms/{id}")
    @Timed
    public ResponseEntity<Void> deleteClassRoom(@PathVariable UUID id) {
        log.debug("REST request to delete ClassRoom : {}", id);
        classRoomService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/class-rooms?query=:query : search for the classRoom corresponding
     * to the query.
     *
     * @param query the query of the classRoom search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/class-rooms")
    @Timed
    public ResponseEntity<List<ClassRoomDTO>> searchClassRooms(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ClassRooms for query {}", query);
        Page<ClassRoomDTO> page = classRoomService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/class-rooms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
