package id.eara.service;

import id.eara.domain.ExtraCourse;
import id.eara.repository.ExtraCourseRepository;
import id.eara.repository.search.ExtraCourseSearchRepository;
import id.eara.service.dto.ExtraCourseDTO;
import id.eara.service.mapper.ExtraCourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ExtraCourse.
 * atiila consulting
 */

@Service
@Transactional
public class ExtraCourseService {

    private final Logger log = LoggerFactory.getLogger(ExtraCourseService.class);

    private final ExtraCourseRepository extraCourseRepository;

    private final ExtraCourseMapper extraCourseMapper;

    private final ExtraCourseSearchRepository extraCourseSearchRepository;

    public ExtraCourseService(ExtraCourseRepository extraCourseRepository, ExtraCourseMapper extraCourseMapper, ExtraCourseSearchRepository extraCourseSearchRepository) {
        this.extraCourseRepository = extraCourseRepository;
        this.extraCourseMapper = extraCourseMapper;
        this.extraCourseSearchRepository = extraCourseSearchRepository;
    }

    /**
     * Save a extraCourse.
     *
     * @param extraCourseDTO the entity to save
     * @return the persisted entity
     */
    public ExtraCourseDTO save(ExtraCourseDTO extraCourseDTO) {
        log.debug("Request to save ExtraCourse : {}", extraCourseDTO);
        ExtraCourse extraCourse = extraCourseMapper.toEntity(extraCourseDTO);
        extraCourse = extraCourseRepository.save(extraCourse);
        ExtraCourseDTO result = extraCourseMapper.toDto(extraCourse);
        extraCourseSearchRepository.save(extraCourse);
        return result;
    }

    /**
     *  Get all the extraCourses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtraCourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExtraCourses");
        return extraCourseRepository.findAll(pageable)
            .map(extraCourseMapper::toDto);
    }

    /**
     *  Get one extraCourse by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ExtraCourseDTO findOne(UUID id) {
        log.debug("Request to get ExtraCourse : {}", id);
        ExtraCourse extraCourse = extraCourseRepository.findOne(id);
        return extraCourseMapper.toDto(extraCourse);
    }

    /**
     *  Delete the  extraCourse by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ExtraCourse : {}", id);
        extraCourseRepository.delete(id);
        extraCourseSearchRepository.delete(id);
    }

    /**
     * Search for the extraCourse corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ExtraCourseDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExtraCourses for query {}", query);
        Page<ExtraCourse> result = extraCourseSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(extraCourseMapper::toDto);
    }
}
