package com.example.technical.test.service;

import com.example.technical.test.model.Appointment;


import java.util.List;

public interface IAppointmentService {
    List<Appointment> getAppointments(String filename);

    void setAppointments(List<Appointment> appointments);

    void setAppointments();

    void runEvery30Seconds();
}
