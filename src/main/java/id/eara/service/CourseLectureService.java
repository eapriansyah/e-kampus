package id.eara.service;

import id.eara.domain.CourseLecture;
import id.eara.repository.CourseLectureRepository;
import id.eara.repository.search.CourseLectureSearchRepository;
import id.eara.service.dto.CourseLectureDTO;
import id.eara.service.mapper.CourseLectureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing CourseLecture.
 * atiila consulting
 */

@Service
@Transactional
public class CourseLectureService {

    private final Logger log = LoggerFactory.getLogger(CourseLectureService.class);

    private final CourseLectureRepository courseLectureRepository;

    private final CourseLectureMapper courseLectureMapper;

    private final CourseLectureSearchRepository courseLectureSearchRepository;

    public CourseLectureService(CourseLectureRepository courseLectureRepository, CourseLectureMapper courseLectureMapper, CourseLectureSearchRepository courseLectureSearchRepository) {
        this.courseLectureRepository = courseLectureRepository;
        this.courseLectureMapper = courseLectureMapper;
        this.courseLectureSearchRepository = courseLectureSearchRepository;
    }

    /**
     * Save a courseLecture.
     *
     * @param courseLectureDTO the entity to save
     * @return the persisted entity
     */
    public CourseLectureDTO save(CourseLectureDTO courseLectureDTO) {
        log.debug("Request to save CourseLecture : {}", courseLectureDTO);
        CourseLecture courseLecture = courseLectureMapper.toEntity(courseLectureDTO);
        courseLecture = courseLectureRepository.save(courseLecture);
        CourseLectureDTO result = courseLectureMapper.toDto(courseLecture);
        courseLectureSearchRepository.save(courseLecture);
        return result;
    }

    /**
     *  Get all the courseLectures.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourseLectureDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseLectures");
        return courseLectureRepository.findAll(pageable)
            .map(courseLectureMapper::toDto);
    }

    /**
     *  Get one courseLecture by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CourseLectureDTO findOne(UUID id) {
        log.debug("Request to get CourseLecture : {}", id);
        CourseLecture courseLecture = courseLectureRepository.findOne(id);
        return courseLectureMapper.toDto(courseLecture);
    }

    /**
     *  Delete the  courseLecture by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete CourseLecture : {}", id);
        courseLectureRepository.delete(id);
        courseLectureSearchRepository.delete(id);
    }

    /**
     * Search for the courseLecture corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CourseLectureDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CourseLectures for query {}", query);
        Page<CourseLecture> result = courseLectureSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(courseLectureMapper::toDto);
    }
}
