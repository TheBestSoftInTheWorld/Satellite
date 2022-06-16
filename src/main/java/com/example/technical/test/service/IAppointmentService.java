package com.example.technical.test.service;

import com.example.technical.test.model.Appointment;

import java.util.Date;
import java.util.List;

public interface IAppointmentService {
    List<Appointment> getAppointments(Date from);
}
