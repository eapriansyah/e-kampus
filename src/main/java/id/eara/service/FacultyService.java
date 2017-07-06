package id.eara.service;

import id.eara.domain.Faculty;
import id.eara.repository.FacultyRepository;
import id.eara.repository.search.FacultySearchRepository;
import id.eara.service.dto.FacultyDTO;
import id.eara.service.mapper.FacultyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing Faculty.
 * atiila consulting
 */

@Service
@Transactional
public class FacultyService {

    private final Logger log = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    private final FacultyMapper facultyMapper;

    private final FacultySearchRepository facultySearchRepository;

    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, FacultySearchRepository facultySearchRepository) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.facultySearchRepository = facultySearchRepository;
    }

    /**
     * Save a faculty.
     *
     * @param facultyDTO the entity to save
     * @return the persisted entity
     */
    public FacultyDTO save(FacultyDTO facultyDTO) {
        log.debug("Request to save Faculty : {}", facultyDTO);
        Faculty faculty = facultyMapper.toEntity(facultyDTO);
        faculty = facultyRepository.save(faculty);
        FacultyDTO result = facultyMapper.toDto(faculty);
        facultySearchRepository.save(faculty);
        return result;
    }

    /**
     *  Get all the faculties.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FacultyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Faculties");
        return facultyRepository.findAll(pageable)
            .map(facultyMapper::toDto);
    }

    /**
     *  Get one faculty by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public FacultyDTO findOne(UUID id) {
        log.debug("Request to get Faculty : {}", id);
        Faculty faculty = facultyRepository.findOne(id);
        return facultyMapper.toDto(faculty);
    }

    /**
     *  Delete the  faculty by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete Faculty : {}", id);
        facultyRepository.delete(id);
        facultySearchRepository.delete(id);
    }

    /**
     * Search for the faculty corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<FacultyDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Faculties for query {}", query);
        Page<Faculty> result = facultySearchRepository.search(queryStringQuery(query), pageable);
        return result.map(facultyMapper::toDto);
    }
}
