package org.lh.service.impl;

import org.lh.service.EventCheckInService;
import org.lh.domain.EventCheckIn;
import org.lh.repository.EventCheckInRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EventCheckIn}.
 */
@Service
@Transactional
public class EventCheckInServiceImpl implements EventCheckInService {

    private final Logger log = LoggerFactory.getLogger(EventCheckInServiceImpl.class);

    private final EventCheckInRepository eventCheckInRepository;

    public EventCheckInServiceImpl(EventCheckInRepository eventCheckInRepository) {
        this.eventCheckInRepository = eventCheckInRepository;
    }

    /**
     * Save a eventCheckIn.
     *
     * @param eventCheckIn the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EventCheckIn save(EventCheckIn eventCheckIn) {
        log.debug("Request to save EventCheckIn : {}", eventCheckIn);
        return eventCheckInRepository.save(eventCheckIn);
    }

    /**
     * Get all the eventCheckIns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EventCheckIn> findAll(Pageable pageable) {
        log.debug("Request to get all EventCheckIns");
        return eventCheckInRepository.findAll(pageable);
    }


    /**
     * Get one eventCheckIn by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EventCheckIn> findOne(Long id) {
        log.debug("Request to get EventCheckIn : {}", id);
        return eventCheckInRepository.findById(id);
    }

    /**
     * Delete the eventCheckIn by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventCheckIn : {}", id);
        eventCheckInRepository.deleteById(id);
    }
}
