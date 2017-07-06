package id.eara.service;

import id.eara.domain.ContactMechanism;
import id.eara.repository.ContactMechanismRepository;
import id.eara.repository.search.ContactMechanismSearchRepository;
import id.eara.service.dto.ContactMechanismDTO;
import id.eara.service.mapper.ContactMechanismMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.UUID;


/**
 * Service Implementation for managing ContactMechanism.
 * atiila consulting
 */

@Service
@Transactional
public class ContactMechanismService {

    private final Logger log = LoggerFactory.getLogger(ContactMechanismService.class);

    private final ContactMechanismRepository contactMechanismRepository;

    private final ContactMechanismMapper contactMechanismMapper;

    private final ContactMechanismSearchRepository contactMechanismSearchRepository;

    public ContactMechanismService(ContactMechanismRepository contactMechanismRepository, ContactMechanismMapper contactMechanismMapper, ContactMechanismSearchRepository contactMechanismSearchRepository) {
        this.contactMechanismRepository = contactMechanismRepository;
        this.contactMechanismMapper = contactMechanismMapper;
        this.contactMechanismSearchRepository = contactMechanismSearchRepository;
    }

    /**
     * Save a contactMechanism.
     *
     * @param contactMechanismDTO the entity to save
     * @return the persisted entity
     */
    public ContactMechanismDTO save(ContactMechanismDTO contactMechanismDTO) {
        log.debug("Request to save ContactMechanism : {}", contactMechanismDTO);
        ContactMechanism contactMechanism = contactMechanismMapper.toEntity(contactMechanismDTO);
        contactMechanism = contactMechanismRepository.save(contactMechanism);
        ContactMechanismDTO result = contactMechanismMapper.toDto(contactMechanism);
        contactMechanismSearchRepository.save(contactMechanism);
        return result;
    }

    /**
     *  Get all the contactMechanisms.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactMechanismDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ContactMechanisms");
        return contactMechanismRepository.findAll(pageable)
            .map(contactMechanismMapper::toDto);
    }

    /**
     *  Get one contactMechanism by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public ContactMechanismDTO findOne(UUID id) {
        log.debug("Request to get ContactMechanism : {}", id);
        ContactMechanism contactMechanism = contactMechanismRepository.findOne(id);
        return contactMechanismMapper.toDto(contactMechanism);
    }

    /**
     *  Delete the  contactMechanism by id.
     *
     *  @param id the id of the entity
     */
    public void delete(UUID id) {
        log.debug("Request to delete ContactMechanism : {}", id);
        contactMechanismRepository.delete(id);
        contactMechanismSearchRepository.delete(id);
    }

    /**
     * Search for the contactMechanism corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContactMechanismDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ContactMechanisms for query {}", query);
        Page<ContactMechanism> result = contactMechanismSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(contactMechanismMapper::toDto);
    }
}
