package org.lh.repository;

import org.lh.domain.EventCheckIn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the EventCheckIn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventCheckInRepository extends JpaRepository<EventCheckIn, Long> {
    /**
     * 通过用户名和手机号查找待签到用户
     * @param userName
     * @param phoneNumber
     * @return
     */
    Optional<EventCheckIn> findByUserNameAndPhoneNumber(String userName,String phoneNumber);
}
