package id.eara.service;

import id.eara.domain.CourseApplicable;
import id.eara.repository.CourseApplicableRepository;
import id.eara.repository.search.CourseApplicableSearchRepository;
import id.eara.service.dto.CourseApplicableDTO;
import id.eara.service.mapper.CourseApplicableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing CourseApplicable.
 * atiila consulting
 */

@Service
@Transactional
public class CourseApplicableService {

    private final Logger log = LoggerFactory.getLogger(CourseApplicableService.class);

    private final CourseApplicableRepository courseApplicableRepository;

    private final CourseApplicableMapper courseApplicableMapper;

    private final CourseApplicableSearchRepository courseApplicableSearchRepository;

    public CourseApplicableService(CourseApplicableRepository courseApplicableRepository, CourseApplicableMapper courseApplicableMapper, CourseApplicableSearchRepository courseApplicableSearchRepository) {
        this.courseApplicableRepository = courseApplicableRepository;
        this.courseApplicableMapper = courseApplicableMapper;
        this.courseApplicableSearchRepository = courseApplicableSearchRepository;
    }

    /**
     * Save a courseApplicable.
     *
     * @param courseApplicableDTO the entity to save
     * @return the persisted entity
     */
    public CourseApplicableDTO save(CourseApplicableDTO courseApplicableDTO) {
        log.debug("Request to save CourseApplicable : {}", courseApplicableDTO);
        CourseApplicable courseApplicable = courseApplicableMapper.toEntity(courseApplicableDTO);
        courseApplicable = courseApplicableRepository.save(courseApplicable);
        CourseApplicableDTO result = courseApplicableMapper.toDto(courseApplicable);
        courseApplicableSearchRepository.save(courseApplicable);
        return result;
    }

    /**
     *  Get all the courseApplicables.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourseApplicableDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseApplicables");
        return courseApplicableRepository.findAll(pageable)
            .map(courseApplicableMapper::toDto);
    }

    /**
     *  Get one courseApplicable by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CourseApplicableDTO findOne(UUID id) {
        log.debug("Request to get CourseApplicable : {}", id);
        CourseApplicable courseApplicable = courseApplicableRepository.findOne(id);
        return courseApplicableMapper.toDto(courseApplicable);
    }

    /**
     *  Delete the  courseApplicable by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete CourseApplicable : {}", id);
        courseApplicableRepository.delete(id);
        courseApplicableSearchRepository.delete(id);
    }

    /**
     * Search for the courseApplicable corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourseApplicableDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CourseApplicables for query {}", query);
        Page<CourseApplicable> result = courseApplicableSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(courseApplicableMapper::toDto);
    }
}
