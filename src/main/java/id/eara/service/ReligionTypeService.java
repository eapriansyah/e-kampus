package id.eara.service;

import id.eara.domain.ReligionType;
import id.eara.repository.ReligionTypeRepository;
import id.eara.repository.search.ReligionTypeSearchRepository;
import id.eara.service.dto.ReligionTypeDTO;
import id.eara.service.mapper.ReligionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;


/**
 * Service Implementation for managing ReligionType.
 * atiila consulting
 */

@Service
@Transactional
public class ReligionTypeService {

    private final Logger log = LoggerFactory.getLogger(ReligionTypeService.class);

    private final ReligionTypeRepository religionTypeRepository;

    private final ReligionTypeMapper religionTypeMapper;

    private final ReligionTypeSearchRepository religionTypeSearchRepository;

    public ReligionTypeService(ReligionTypeRepository religionTypeRepository, ReligionTypeMapper religionTypeMapper, ReligionTypeSearchRepository religionTypeSearchRepository) {
        this.religionTypeRepository = religionTypeRepository;
        this.religionTypeMapper = religionTypeMapper;
        this.religionTypeSearchRepository = religionTypeSearchRepository;
    }

    /**
     * Save a religionType.
     *
     * @param religionTypeDTO the entity to save
     * @return the persisted entity
     */
    public ReligionTypeDTO save(ReligionTypeDTO religionTypeDTO) {
        log.debug("Request to save ReligionType : {}", religionTypeDTO);
        ReligionType religionType = religionTypeMapper.toEntity(religionTypeDTO);
        religionType = religionTypeRepository.save(religionType);
        ReligionTypeDTO result = religionTypeMapper.toDto(religionType);
        religionTypeSearchRepository.save(religionType);
        return result;
    }

    /**
     *  Get all the religionTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReligionTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ReligionTypes");
        return religionTypeRepository.findAll(pageable)
            .map(religionTypeMapper::toDto);
    }

    /**
     *  Get one religionType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ReligionTypeDTO findOne(Long id) {
        log.debug("Request to get ReligionType : {}", id);
        ReligionType religionType = religionTypeRepository.findOne(id);
        return religionTypeMapper.toDto(religionType);
    }

    /**
     *  Delete the  religionType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ReligionType : {}", id);
        religionTypeRepository.delete(id);
        religionTypeSearchRepository.delete(id);
    }

    /**
     * Search for the religionType corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ReligionTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ReligionTypes for query {}", query);
        Page<ReligionType> result = religionTypeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(religionTypeMapper::toDto);
    }
}
