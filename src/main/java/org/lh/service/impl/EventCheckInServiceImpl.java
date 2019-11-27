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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Service Implementation for managing {@link EventCheckIn}.
 */
@Service
@Transactional
public class EventCheckInServiceImpl implements EventCheckInService {

    private final Logger log = LoggerFactory.getLogger(EventCheckInServiceImpl.class);

    private final EventCheckInRepository eventCheckInRepository;

    private final ZoneId systemDefault = ZoneId.systemDefault();

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

    /**
     * 签到业务逻辑
     * @param userName
     * @param phoneNumber
     * @return
     */
    @Override
    public String findByUserNameAndPhoneNumber(String userName, String phoneNumber) {
        Optional<EventCheckIn> queryResult = eventCheckInRepository.findByUserNameAndPhoneNumber(userName,phoneNumber);

        EventCheckIn checkIn = null;

        if (queryResult.isPresent()){
            checkIn = queryResult.get();
        }else{
            log.info("Get from database error");
        }

        if (checkIn != null && checkIn.isIsCheckIn()) {
            return "签到失败:已经签到过了，请勿重复签到";
        }else if (checkIn != null && !checkIn.isIsCheckIn()){
            ZonedDateTime newTime = ZonedDateTime.ofInstant(Instant.now(),systemDefault);
            checkIn.setIsCheckIn(true);
            checkIn.setCheckTime(newTime);
            eventCheckInRepository.save(checkIn);
            return "签到成功:签到时间 "+newTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"));
        }else
            return "签到失败:请检查用户名和手机号是否正确";
    }
}
