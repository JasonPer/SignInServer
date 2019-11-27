package org.lh.repository;

import org.lh.domain.EventCheckIn;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EventCheckIn entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventCheckInRepository extends JpaRepository<EventCheckIn, Long> {

}
