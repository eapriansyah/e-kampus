package id.eara.service;

import id.eara.domain.WorkType;
import id.eara.repository.WorkTypeRepository;
import id.eara.repository.search.WorkTypeSearchRepository;
import id.eara.service.dto.WorkTypeDTO;
import id.eara.service.mapper.WorkTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing WorkType.
 * atiila consulting
 */

@Service
@Transactional
public class WorkTypeService {

    private final Logger log = LoggerFactory.getLogger(WorkTypeService.class);

    private final WorkTypeRepository workTypeRepository;

    private final WorkTypeMapper workTypeMapper;

    private final WorkTypeSearchRepository workTypeSearchRepository;

    public WorkTypeService(WorkTypeRepository workTypeRepository, WorkTypeMapper workTypeMapper, WorkTypeSearchRepository workTypeSearchRepository) {
        this.workTypeRepository = workTypeRepository;
        this.workTypeMapper = workTypeMapper;
        this.workTypeSearchRepository = workTypeSearchRepository;
    }

    /**
     * Save a workType.
     *
     * @param workTypeDTO the entity to save
     * @return the persisted entity
     */
    public WorkTypeDTO save(WorkTypeDTO workTypeDTO) {
        log.debug("Request to save WorkType : {}", workTypeDTO);
        WorkType workType = workTypeMapper.toEntity(workTypeDTO);
        workType = workTypeRepository.save(workType);
        WorkTypeDTO result = workTypeMapper.toDto(workType);
        workTypeSearchRepository.save(workType);
        return result;
    }

    /**
     *  Get all the workTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WorkTypes");
        return workTypeRepository.findAll(pageable)
            .map(workTypeMapper::toDto);
    }

    /**
     *  Get one workType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public WorkTypeDTO findOne(Long id) {
        log.debug("Request to get WorkType : {}", id);
        WorkType workType = workTypeRepository.findOne(id);
        return workTypeMapper.toDto(workType);
    }

    /**
     *  Delete the  workType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkType : {}", id);
        workTypeRepository.delete(id);
        workTypeSearchRepository.delete(id);
    }

    /**
     * Search for the workType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<WorkTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of WorkTypes for query {}", query);
        Page<WorkType> result = workTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(workTypeMapper::toDto);
    }
}
