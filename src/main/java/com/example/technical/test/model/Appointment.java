package com.example.technical.test.model;

import java.util.Date;

public class Appointment {
    long id;
    Date appointmentTime;
    long personId;
    Date modified;
    String reason;
    StateEnum state;

    public Appointment(Date appointmentTime, long personId, Date modified, String reason, StateEnum state) {
        this.appointmentTime = appointmentTime;
        this.personId = personId;
        this.modified = modified;
        this.reason = reason;
        this.state = state;
    }

    public Appointment(long id, Date appointmentTime, long personId, Date modified, String reason, StateEnum state) {
        this.id = id;
        this.appointmentTime = appointmentTime;
        this.personId = personId;
        this.modified = modified;
        this.reason = reason;
        this.state = state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Date appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }
}
