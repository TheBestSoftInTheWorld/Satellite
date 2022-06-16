package com.example.technical.test.service;

import com.example.technical.test.dao.IAppointmentDAO;
import com.example.technical.test.model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentService implements IAppointmentService {
    @Autowired
    IAppointmentDAO iAppointmentDAO;

    @Override
    public List<Appointment> getAppointments(Date from) {
        List<Appointment> appointments = iAppointmentDAO.getAppointments(from);
        return appointments;
    }
}
