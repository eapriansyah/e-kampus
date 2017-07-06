package id.eara.service;

import id.eara.domain.PostStudent;
import id.eara.repository.PostStudentRepository;
import id.eara.repository.search.PostStudentSearchRepository;
import id.eara.service.dto.PostStudentDTO;
import id.eara.service.mapper.PostStudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing PostStudent.
 * atiila consulting
 */

@Service
@Transactional
public class PostStudentService {

    private final Logger log = LoggerFactory.getLogger(PostStudentService.class);

    private final PostStudentRepository postStudentRepository;

    private final PostStudentMapper postStudentMapper;

    private final PostStudentSearchRepository postStudentSearchRepository;

    public PostStudentService(PostStudentRepository postStudentRepository, PostStudentMapper postStudentMapper, PostStudentSearchRepository postStudentSearchRepository) {
        this.postStudentRepository = postStudentRepository;
        this.postStudentMapper = postStudentMapper;
        this.postStudentSearchRepository = postStudentSearchRepository;
    }

    /**
     * Save a postStudent.
     *
     * @param postStudentDTO the entity to save
     * @return the persisted entity
     */
    public PostStudentDTO save(PostStudentDTO postStudentDTO) {
        log.debug("Request to save PostStudent : {}", postStudentDTO);
        PostStudent postStudent = postStudentMapper.toEntity(postStudentDTO);
        postStudent = postStudentRepository.save(postStudent);
        PostStudentDTO result = postStudentMapper.toDto(postStudent);
        postStudentSearchRepository.save(postStudent);
        return result;
    }

    /**
     *  Get all the postStudents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostStudentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PostStudents");
        return postStudentRepository.findAll(pageable)
            .map(postStudentMapper::toDto);
    }

    /**
     *  Get one postStudent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PostStudentDTO findOne(UUID id) {
        log.debug("Request to get PostStudent : {}", id);
        PostStudent postStudent = postStudentRepository.findOne(id);
        return postStudentMapper.toDto(postStudent);
    }

    /**
     *  Delete the  postStudent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete PostStudent : {}", id);
        postStudentRepository.delete(id);
        postStudentSearchRepository.delete(id);
    }

    /**
     * Search for the postStudent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PostStudentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PostStudents for query {}", query);
        Page<PostStudent> result = postStudentSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(postStudentMapper::toDto);
    }
}
