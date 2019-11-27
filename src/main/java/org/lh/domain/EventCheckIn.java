package org.lh.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A EventCheckIn.
 */
@Entity
@Table(name = "event_check_in")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EventCheckIn implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "address")
    private String address;

    @Column(name = "check_time")
    private ZonedDateTime checkTime;

    @Column(name = "is_check_in")
    private Boolean isCheckIn;

    @Column(name = "phone_number")
    private String phoneNumber;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public EventCheckIn userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public EventCheckIn address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ZonedDateTime getCheckTime() {
        return checkTime;
    }

    public EventCheckIn checkTime(ZonedDateTime checkTime) {
        this.checkTime = checkTime;
        return this;
    }

    public void setCheckTime(ZonedDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public Boolean isIsCheckIn() {
        return isCheckIn;
    }

    public EventCheckIn isCheckIn(Boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
        return this;
    }

    public void setIsCheckIn(Boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public EventCheckIn phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventCheckIn)) {
            return false;
        }
        return id != null && id.equals(((EventCheckIn) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EventCheckIn{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", address='" + getAddress() + "'" +
            ", checkTime='" + getCheckTime() + "'" +
            ", isCheckIn='" + isIsCheckIn() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
