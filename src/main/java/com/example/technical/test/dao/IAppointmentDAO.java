package com.example.technical.test.dao;

import com.example.technical.test.model.Appointment;

import java.util.Date;
import java.util.List;

public interface IAppointmentDAO {
    List<Appointment> getAppointments(Date from);
}
