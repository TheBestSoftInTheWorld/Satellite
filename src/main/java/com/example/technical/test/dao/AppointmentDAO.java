package com.example.technical.test.dao;

import com.example.technical.test.model.Appointment;
import com.example.technical.test.model.StateEnum;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Repository
public class AppointmentDAO implements IAppointmentDAO {
    public final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public List<Appointment> getAppointments() {
        try {
            List<Appointment> appointmentsList = new ArrayList<>();
            File file = ResourceUtils.getFile("classpath:appointment.csv");
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                String text = input.nextLine();
                String[] result = text.split(",");
                if (result.length == 5) {
                    Date date = stringToDate(result[0]);
                    Date modified = stringToDate(result[2]);
                    Appointment appointment = new Appointment(date, Long.parseLong(result[1]), modified, result[3], StateEnum.valueOf(result[4].toUpperCase()));
                    appointmentsList.add(appointment);
                }
            }
            return appointmentsList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date stringToDate(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);
            return formatter.parse(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }
}
