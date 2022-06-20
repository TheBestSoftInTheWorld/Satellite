package com.example.technical.test.service;

import com.example.technical.test.model.Appointment;


import java.util.List;

public interface IAppointmentService {
    List<Appointment> getAppointments();

    List<Appointment> generateAppointments(int size);

    void setAppointments(List<Appointment> appointments);

    void setRandomAppointments();
}
