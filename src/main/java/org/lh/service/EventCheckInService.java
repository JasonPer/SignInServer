package org.lh.service;

import org.lh.domain.EventCheckIn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
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

    /**
     * 通过用户名和手机号查找待签到用户
     * @param userName
     * @param phoneNumber
     * @return
     */
    String findByUserNameAndPhoneNumber(String userName,String phoneNumber);

    /**
     * 获取全部人员信息
     * @return
     */
    List<EventCheckIn> getAllPersonDetails();
}
