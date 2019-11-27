package org.lh.web.rest;

import org.lh.domain.EventCheckIn;
import org.lh.service.EventCheckInService;
import org.lh.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.lh.domain.EventCheckIn}.
 */
@RestController
@RequestMapping("/api")
public class EventCheckInResource {

    private final Logger log = LoggerFactory.getLogger(EventCheckInResource.class);

    private static final String ENTITY_NAME = "eventCheckIn";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventCheckInService eventCheckInService;

    public EventCheckInResource(EventCheckInService eventCheckInService) {
        this.eventCheckInService = eventCheckInService;
    }

    /**
     * {@code POST  /event-check-ins} : Create a new eventCheckIn.
     *
     * @param eventCheckIn the eventCheckIn to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventCheckIn, or with status {@code 400 (Bad Request)} if the eventCheckIn has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-check-ins")
    public ResponseEntity<EventCheckIn> createEventCheckIn(@RequestBody EventCheckIn eventCheckIn) throws URISyntaxException {
        log.debug("REST request to save EventCheckIn : {}", eventCheckIn);
        if (eventCheckIn.getId() != null) {
            throw new BadRequestAlertException("A new eventCheckIn cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventCheckIn result = eventCheckInService.save(eventCheckIn);
        return ResponseEntity.created(new URI("/api/event-check-ins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-check-ins} : Updates an existing eventCheckIn.
     *
     * @param eventCheckIn the eventCheckIn to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventCheckIn,
     * or with status {@code 400 (Bad Request)} if the eventCheckIn is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventCheckIn couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-check-ins")
    public ResponseEntity<EventCheckIn> updateEventCheckIn(@RequestBody EventCheckIn eventCheckIn) throws URISyntaxException {
        log.debug("REST request to update EventCheckIn : {}", eventCheckIn);
        if (eventCheckIn.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EventCheckIn result = eventCheckInService.save(eventCheckIn);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventCheckIn.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /event-check-ins} : get all the eventCheckIns.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventCheckIns in body.
     */
    @GetMapping("/event-check-ins")
    public ResponseEntity<List<EventCheckIn>> getAllEventCheckIns(Pageable pageable) {
        log.debug("REST request to get a page of EventCheckIns");
        Page<EventCheckIn> page = eventCheckInService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-check-ins/:id} : get the "id" eventCheckIn.
     *
     * @param id the id of the eventCheckIn to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventCheckIn, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-check-ins/{id}")
    public ResponseEntity<EventCheckIn> getEventCheckIn(@PathVariable Long id) {
        log.debug("REST request to get EventCheckIn : {}", id);
        Optional<EventCheckIn> eventCheckIn = eventCheckInService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventCheckIn);
    }

    /**
     * {@code DELETE  /event-check-ins/:id} : delete the "id" eventCheckIn.
     *
     * @param id the id of the eventCheckIn to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-check-ins/{id}")
    public ResponseEntity<Void> deleteEventCheckIn(@PathVariable Long id) {
        log.debug("REST request to delete EventCheckIn : {}", id);
        eventCheckInService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * 通过用户名和手机进行签到
     * @param userName
     * @param phoneNumber
     * @return
     */
    @PostMapping("/check_in")
    public ResponseEntity isCheckIn(String userName,String phoneNumber){
        String result = eventCheckInService.findByUserNameAndPhoneNumber(userName,phoneNumber);
        return ResponseEntity.ok(result);
    }
}
