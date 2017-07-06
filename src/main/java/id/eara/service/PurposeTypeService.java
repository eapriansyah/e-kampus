package id.eara.service;

import id.eara.domain.PurposeType;
import id.eara.repository.PurposeTypeRepository;
import id.eara.repository.search.PurposeTypeSearchRepository;
import id.eara.service.dto.PurposeTypeDTO;
import id.eara.service.mapper.PurposeTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing PurposeType.
 * atiila consulting
 */

@Service
@Transactional
public class PurposeTypeService {

    private final Logger log = LoggerFactory.getLogger(PurposeTypeService.class);

    private final PurposeTypeRepository purposeTypeRepository;

    private final PurposeTypeMapper purposeTypeMapper;

    private final PurposeTypeSearchRepository purposeTypeSearchRepository;

    public PurposeTypeService(PurposeTypeRepository purposeTypeRepository, PurposeTypeMapper purposeTypeMapper, PurposeTypeSearchRepository purposeTypeSearchRepository) {
        this.purposeTypeRepository = purposeTypeRepository;
        this.purposeTypeMapper = purposeTypeMapper;
        this.purposeTypeSearchRepository = purposeTypeSearchRepository;
    }

    /**
     * Save a purposeType.
     *
     * @param purposeTypeDTO the entity to save
     * @return the persisted entity
     */
    public PurposeTypeDTO save(PurposeTypeDTO purposeTypeDTO) {
        log.debug("Request to save PurposeType : {}", purposeTypeDTO);
        PurposeType purposeType = purposeTypeMapper.toEntity(purposeTypeDTO);
        purposeType = purposeTypeRepository.save(purposeType);
        PurposeTypeDTO result = purposeTypeMapper.toDto(purposeType);
        purposeTypeSearchRepository.save(purposeType);
        return result;
    }

    /**
     *  Get all the purposeTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PurposeTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PurposeTypes");
        return purposeTypeRepository.findAll(pageable)
            .map(purposeTypeMapper::toDto);
    }

    /**
     *  Get one purposeType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public PurposeTypeDTO findOne(Long id) {
        log.debug("Request to get PurposeType : {}", id);
        PurposeType purposeType = purposeTypeRepository.findOne(id);
        return purposeTypeMapper.toDto(purposeType);
    }

    /**
     *  Delete the  purposeType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PurposeType : {}", id);
        purposeTypeRepository.delete(id);
        purposeTypeSearchRepository.delete(id);
    }

    /**
     * Search for the purposeType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PurposeTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of PurposeTypes for query {}", query);
        Page<PurposeType> result = purposeTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(purposeTypeMapper::toDto);
    }
}
