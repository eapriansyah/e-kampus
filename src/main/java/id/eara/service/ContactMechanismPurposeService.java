package id.eara.service;

import id.eara.domain.ContactMechanismPurpose;
import id.eara.repository.ContactMechanismPurposeRepository;
import id.eara.repository.search.ContactMechanismPurposeSearchRepository;
import id.eara.service.dto.ContactMechanismPurposeDTO;
import id.eara.service.mapper.ContactMechanismPurposeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ContactMechanismPurpose.
 * atiila consulting
 */

@Service
@Transactional
public class ContactMechanismPurposeService {

    private final Logger log = LoggerFactory.getLogger(ContactMechanismPurposeService.class);

    private final ContactMechanismPurposeRepository contactMechanismPurposeRepository;

    private final ContactMechanismPurposeMapper contactMechanismPurposeMapper;

    private final ContactMechanismPurposeSearchRepository contactMechanismPurposeSearchRepository;

    public ContactMechanismPurposeService(ContactMechanismPurposeRepository contactMechanismPurposeRepository, ContactMechanismPurposeMapper contactMechanismPurposeMapper, ContactMechanismPurposeSearchRepository contactMechanismPurposeSearchRepository) {
        this.contactMechanismPurposeRepository = contactMechanismPurposeRepository;
        this.contactMechanismPurposeMapper = contactMechanismPurposeMapper;
        this.contactMechanismPurposeSearchRepository = contactMechanismPurposeSearchRepository;
    }

    /**
     * Save a contactMechanismPurpose.
     *
     * @param contactMechanismPurposeDTO the entity to save
     * @return the persisted entity
     */
    public ContactMechanismPurposeDTO save(ContactMechanismPurposeDTO contactMechanismPurposeDTO) {
        log.debug("Request to save ContactMechanismPurpose : {}", contactMechanismPurposeDTO);
        ContactMechanismPurpose contactMechanismPurpose = contactMechanismPurposeMapper.toEntity(contactMechanismPurposeDTO);
        contactMechanismPurpose = contactMechanismPurposeRepository.save(contactMechanismPurpose);
        ContactMechanismPurposeDTO result = contactMechanismPurposeMapper.toDto(contactMechanismPurpose);
        contactMechanismPurposeSearchRepository.save(contactMechanismPurpose);
        return result;
    }

    /**
     *  Get all the contactMechanismPurposes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactMechanismPurposeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactMechanismPurposes");
        return contactMechanismPurposeRepository.findAll(pageable)
            .map(contactMechanismPurposeMapper::toDto);
    }

    /**
     *  Get one contactMechanismPurpose by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ContactMechanismPurposeDTO findOne(UUID id) {
        log.debug("Request to get ContactMechanismPurpose : {}", id);
        ContactMechanismPurpose contactMechanismPurpose = contactMechanismPurposeRepository.findOne(id);
        return contactMechanismPurposeMapper.toDto(contactMechanismPurpose);
    }

    /**
     *  Delete the  contactMechanismPurpose by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ContactMechanismPurpose : {}", id);
        contactMechanismPurposeRepository.delete(id);
        contactMechanismPurposeSearchRepository.delete(id);
    }

    /**
     * Search for the contactMechanismPurpose corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactMechanismPurposeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactMechanismPurposes for query {}", query);
        Page<ContactMechanismPurpose> result = contactMechanismPurposeSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(contactMechanismPurposeMapper::toDto);
    }
}
