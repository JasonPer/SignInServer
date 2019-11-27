package org.lh.web.rest;

import org.lh.SignInServerApp;
import org.lh.domain.EventCheckIn;
import org.lh.repository.EventCheckInRepository;
import org.lh.service.EventCheckInService;
import org.lh.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.lh.web.rest.TestUtil.sameInstant;
import static org.lh.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EventCheckInResource} REST controller.
 */
@SpringBootTest(classes = SignInServerApp.class)
public class EventCheckInResourceIT {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CHECK_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CHECK_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CHECK_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_IS_CHECK_IN = false;
    private static final Boolean UPDATED_IS_CHECK_IN = true;

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    @Autowired
    private EventCheckInRepository eventCheckInRepository;

    @Autowired
    private EventCheckInService eventCheckInService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restEventCheckInMockMvc;

    private EventCheckIn eventCheckIn;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventCheckInResource eventCheckInResource = new EventCheckInResource(eventCheckInService);
        this.restEventCheckInMockMvc = MockMvcBuilders.standaloneSetup(eventCheckInResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCheckIn createEntity(EntityManager em) {
        EventCheckIn eventCheckIn = new EventCheckIn()
            .userName(DEFAULT_USER_NAME)
            .address(DEFAULT_ADDRESS)
            .checkTime(DEFAULT_CHECK_TIME)
            .isCheckIn(DEFAULT_IS_CHECK_IN)
            .phoneNumber(DEFAULT_PHONE_NUMBER);
        return eventCheckIn;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventCheckIn createUpdatedEntity(EntityManager em) {
        EventCheckIn eventCheckIn = new EventCheckIn()
            .userName(UPDATED_USER_NAME)
            .address(UPDATED_ADDRESS)
            .checkTime(UPDATED_CHECK_TIME)
            .isCheckIn(UPDATED_IS_CHECK_IN)
            .phoneNumber(UPDATED_PHONE_NUMBER);
        return eventCheckIn;
    }

    @BeforeEach
    public void initTest() {
        eventCheckIn = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventCheckIn() throws Exception {
        int databaseSizeBeforeCreate = eventCheckInRepository.findAll().size();

        // Create the EventCheckIn
        restEventCheckInMockMvc.perform(post("/api/event-check-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCheckIn)))
            .andExpect(status().isCreated());

        // Validate the EventCheckIn in the database
        List<EventCheckIn> eventCheckInList = eventCheckInRepository.findAll();
        assertThat(eventCheckInList).hasSize(databaseSizeBeforeCreate + 1);
        EventCheckIn testEventCheckIn = eventCheckInList.get(eventCheckInList.size() - 1);
        assertThat(testEventCheckIn.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEventCheckIn.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testEventCheckIn.getCheckTime()).isEqualTo(DEFAULT_CHECK_TIME);
        assertThat(testEventCheckIn.isIsCheckIn()).isEqualTo(DEFAULT_IS_CHECK_IN);
        assertThat(testEventCheckIn.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void createEventCheckInWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventCheckInRepository.findAll().size();

        // Create the EventCheckIn with an existing ID
        eventCheckIn.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventCheckInMockMvc.perform(post("/api/event-check-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCheckIn)))
            .andExpect(status().isBadRequest());

        // Validate the EventCheckIn in the database
        List<EventCheckIn> eventCheckInList = eventCheckInRepository.findAll();
        assertThat(eventCheckInList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEventCheckIns() throws Exception {
        // Initialize the database
        eventCheckInRepository.saveAndFlush(eventCheckIn);

        // Get all the eventCheckInList
        restEventCheckInMockMvc.perform(get("/api/event-check-ins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventCheckIn.getId().intValue())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].checkTime").value(hasItem(sameInstant(DEFAULT_CHECK_TIME))))
            .andExpect(jsonPath("$.[*].isCheckIn").value(hasItem(DEFAULT_IS_CHECK_IN.booleanValue())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getEventCheckIn() throws Exception {
        // Initialize the database
        eventCheckInRepository.saveAndFlush(eventCheckIn);

        // Get the eventCheckIn
        restEventCheckInMockMvc.perform(get("/api/event-check-ins/{id}", eventCheckIn.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventCheckIn.getId().intValue()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.checkTime").value(sameInstant(DEFAULT_CHECK_TIME)))
            .andExpect(jsonPath("$.isCheckIn").value(DEFAULT_IS_CHECK_IN.booleanValue()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEventCheckIn() throws Exception {
        // Get the eventCheckIn
        restEventCheckInMockMvc.perform(get("/api/event-check-ins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventCheckIn() throws Exception {
        // Initialize the database
        eventCheckInService.save(eventCheckIn);

        int databaseSizeBeforeUpdate = eventCheckInRepository.findAll().size();

        // Update the eventCheckIn
        EventCheckIn updatedEventCheckIn = eventCheckInRepository.findById(eventCheckIn.getId()).get();
        // Disconnect from session so that the updates on updatedEventCheckIn are not directly saved in db
        em.detach(updatedEventCheckIn);
        updatedEventCheckIn
            .userName(UPDATED_USER_NAME)
            .address(UPDATED_ADDRESS)
            .checkTime(UPDATED_CHECK_TIME)
            .isCheckIn(UPDATED_IS_CHECK_IN)
            .phoneNumber(UPDATED_PHONE_NUMBER);

        restEventCheckInMockMvc.perform(put("/api/event-check-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEventCheckIn)))
            .andExpect(status().isOk());

        // Validate the EventCheckIn in the database
        List<EventCheckIn> eventCheckInList = eventCheckInRepository.findAll();
        assertThat(eventCheckInList).hasSize(databaseSizeBeforeUpdate);
        EventCheckIn testEventCheckIn = eventCheckInList.get(eventCheckInList.size() - 1);
        assertThat(testEventCheckIn.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEventCheckIn.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testEventCheckIn.getCheckTime()).isEqualTo(UPDATED_CHECK_TIME);
        assertThat(testEventCheckIn.isIsCheckIn()).isEqualTo(UPDATED_IS_CHECK_IN);
        assertThat(testEventCheckIn.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingEventCheckIn() throws Exception {
        int databaseSizeBeforeUpdate = eventCheckInRepository.findAll().size();

        // Create the EventCheckIn

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventCheckInMockMvc.perform(put("/api/event-check-ins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventCheckIn)))
            .andExpect(status().isBadRequest());

        // Validate the EventCheckIn in the database
        List<EventCheckIn> eventCheckInList = eventCheckInRepository.findAll();
        assertThat(eventCheckInList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventCheckIn() throws Exception {
        // Initialize the database
        eventCheckInService.save(eventCheckIn);

        int databaseSizeBeforeDelete = eventCheckInRepository.findAll().size();

        // Delete the eventCheckIn
        restEventCheckInMockMvc.perform(delete("/api/event-check-ins/{id}", eventCheckIn.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventCheckIn> eventCheckInList = eventCheckInRepository.findAll();
        assertThat(eventCheckInList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventCheckIn.class);
        EventCheckIn eventCheckIn1 = new EventCheckIn();
        eventCheckIn1.setId(1L);
        EventCheckIn eventCheckIn2 = new EventCheckIn();
        eventCheckIn2.setId(eventCheckIn1.getId());
        assertThat(eventCheckIn1).isEqualTo(eventCheckIn2);
        eventCheckIn2.setId(2L);
        assertThat(eventCheckIn1).isNotEqualTo(eventCheckIn2);
        eventCheckIn1.setId(null);
        assertThat(eventCheckIn1).isNotEqualTo(eventCheckIn2);
    }
}
