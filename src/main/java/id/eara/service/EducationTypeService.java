package id.eara.service;

import id.eara.domain.EducationType;
import id.eara.repository.EducationTypeRepository;
import id.eara.repository.search.EducationTypeSearchRepository;
import id.eara.service.dto.EducationTypeDTO;
import id.eara.service.mapper.EducationTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing EducationType.
 * atiila consulting
 */

@Service
@Transactional
public class EducationTypeService {

    private final Logger log = LoggerFactory.getLogger(EducationTypeService.class);

    private final EducationTypeRepository educationTypeRepository;

    private final EducationTypeMapper educationTypeMapper;

    private final EducationTypeSearchRepository educationTypeSearchRepository;

    public EducationTypeService(EducationTypeRepository educationTypeRepository, EducationTypeMapper educationTypeMapper, EducationTypeSearchRepository educationTypeSearchRepository) {
        this.educationTypeRepository = educationTypeRepository;
        this.educationTypeMapper = educationTypeMapper;
        this.educationTypeSearchRepository = educationTypeSearchRepository;
    }

    /**
     * Save a educationType.
     *
     * @param educationTypeDTO the entity to save
     * @return the persisted entity
     */
    public EducationTypeDTO save(EducationTypeDTO educationTypeDTO) {
        log.debug("Request to save EducationType : {}", educationTypeDTO);
        EducationType educationType = educationTypeMapper.toEntity(educationTypeDTO);
        educationType = educationTypeRepository.save(educationType);
        EducationTypeDTO result = educationTypeMapper.toDto(educationType);
        educationTypeSearchRepository.save(educationType);
        return result;
    }

    /**
     *  Get all the educationTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EducationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all EducationTypes");
        return educationTypeRepository.findAll(pageable)
            .map(educationTypeMapper::toDto);
    }

    /**
     *  Get one educationType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EducationTypeDTO findOne(Long id) {
        log.debug("Request to get EducationType : {}", id);
        EducationType educationType = educationTypeRepository.findOne(id);
        return educationTypeMapper.toDto(educationType);
    }

    /**
     *  Delete the  educationType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete EducationType : {}", id);
        educationTypeRepository.delete(id);
        educationTypeSearchRepository.delete(id);
    }

    /**
     * Search for the educationType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EducationTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of EducationTypes for query {}", query);
        Page<EducationType> result = educationTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(educationTypeMapper::toDto);
    }
}
