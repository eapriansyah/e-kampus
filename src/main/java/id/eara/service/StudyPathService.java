package id.eara.service;

import id.eara.domain.StudyPath;
import id.eara.repository.StudyPathRepository;
import id.eara.repository.search.StudyPathSearchRepository;
import id.eara.service.dto.StudyPathDTO;
import id.eara.service.mapper.StudyPathMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing StudyPath.
 * atiila consulting
 */

@Service
@Transactional
public class StudyPathService {

    private final Logger log = LoggerFactory.getLogger(StudyPathService.class);

    private final StudyPathRepository studyPathRepository;

    private final StudyPathMapper studyPathMapper;

    private final StudyPathSearchRepository studyPathSearchRepository;

    public StudyPathService(StudyPathRepository studyPathRepository, StudyPathMapper studyPathMapper, StudyPathSearchRepository studyPathSearchRepository) {
        this.studyPathRepository = studyPathRepository;
        this.studyPathMapper = studyPathMapper;
        this.studyPathSearchRepository = studyPathSearchRepository;
    }

    /**
     * Save a studyPath.
     *
     * @param studyPathDTO the entity to save
     * @return the persisted entity
     */
    public StudyPathDTO save(StudyPathDTO studyPathDTO) {
        log.debug("Request to save StudyPath : {}", studyPathDTO);
        StudyPath studyPath = studyPathMapper.toEntity(studyPathDTO);
        studyPath = studyPathRepository.save(studyPath);
        StudyPathDTO result = studyPathMapper.toDto(studyPath);
        studyPathSearchRepository.save(studyPath);
        return result;
    }

    /**
     *  Get all the studyPaths.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudyPathDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StudyPaths");
        return studyPathRepository.findAll(pageable)
            .map(studyPathMapper::toDto);
    }

    /**
     *  Get one studyPath by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public StudyPathDTO findOne(Long id) {
        log.debug("Request to get StudyPath : {}", id);
        StudyPath studyPath = studyPathRepository.findOne(id);
        return studyPathMapper.toDto(studyPath);
    }

    /**
     *  Delete the  studyPath by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StudyPath : {}", id);
        studyPathRepository.delete(id);
        studyPathSearchRepository.delete(id);
    }

    /**
     * Search for the studyPath corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StudyPathDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of StudyPaths for query {}", query);
        Page<StudyPath> result = studyPathSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(studyPathMapper::toDto);
    }
}
