package id.eara.base;

import java.io.Serializable;

/**
 * Generic REST controller.
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import id.eara.web.rest.util.HeaderUtil;
import id.eara.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

public class EntityRest<ID extends Serializable, E, D extends EntityDTO<E, ID>> {

    protected Logger log;

    protected String ENTITY_NAME = "<none>";
    protected String URL_ENTITY_NAME = "<none>";

    protected IEntityService<ID, E, D> entityService;

    public ResponseEntity<D> create(D d) throws URISyntaxException {
        log.debug("REST request to save " + ENTITY_NAME + " : {}", d);
        if (d.getId() != null) {
            return ResponseEntity.badRequest()
            		.headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new " + ENTITY_NAME +" cannot already have an ID"))
            		.body(null);
        }
        D result = entityService.save(d);
        return ResponseEntity.created(new URI(URL_ENTITY_NAME + "/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<D> update(D d) throws URISyntaxException {
        log.debug("REST request to update " + ENTITY_NAME +" : {}", d);
        if (d.getId() == null) {
            return create(d);
        }
        D result = entityService.save(d);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, d.getId().toString()))
            .body(result);
    }

    public ResponseEntity<List<D>> getAll(Pageable pageable) {
        log.debug("REST request to get a page of " + ENTITY_NAME +"s");
        Page<D> page = entityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, URL_ENTITY_NAME);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    public ResponseEntity<D> get(ID id) {
        log.debug("REST request to get " + ENTITY_NAME +" : {}", id);
        D studentDTO = entityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(studentDTO));
    }

    public ResponseEntity<Void> delete(ID id) {
        log.debug("REST request to delete " + ENTITY_NAME +" : {}", id);
        entityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    public ResponseEntity<List<D>> search(String query, Pageable pageable) {
        log.debug("REST request to search for a page of " + ENTITY_NAME +"s for query {}", query);
        Page<D> page = entityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, URL_ENTITY_NAME);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
