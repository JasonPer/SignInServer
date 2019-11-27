package org.lh.service;

import org.lh.domain.EventCheckIn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link EventCheckIn}.
 */
public interface EventCheckInService {

    /**
     * Save a eventCheckIn.
     *
     * @param eventCheckIn the entity to save.
     * @return the persisted entity.
     */
    EventCheckIn save(EventCheckIn eventCheckIn);

    /**
     * Get all the eventCheckIns.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventCheckIn> findAll(Pageable pageable);


    /**
     * Get the "id" eventCheckIn.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventCheckIn> findOne(Long id);

    /**
     * Delete the "id" eventCheckIn.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
