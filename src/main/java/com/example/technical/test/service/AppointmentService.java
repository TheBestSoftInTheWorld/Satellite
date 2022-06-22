package com.example.technical.test.service;

import com.example.technical.test.dao.IAppointmentDAO;
import com.example.technical.test.model.Appointment;
import com.example.technical.test.model.StateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;


@Service
public class AppointmentService implements IAppointmentService {
    Logger logger = LoggerFactory.getLogger(AppointmentService.class);
    final String API_PERSIST = "http://localhost:8081/api/saveAppointments";


    @Autowired
    IAppointmentDAO iAppointmentDAO;

    @Override
    public List<Appointment> getAppointments(String filename) {
        List<Appointment> appointments = iAppointmentDAO.getAppointments(filename);
        return appointments;
    }

    public void setAppointments() {
        List<Appointment> appointments = iAppointmentDAO.getAppointments("appointments.csv");

        Thread thread = new Thread("Thread") {
            public void run() {
                logger.info("run by: " + getName());

                setAppointments(appointments);
            }
        };
        thread.start();


        try {
            TimeUnit.SECONDS.sleep(30);
            List<Appointment> appointments2 = iAppointmentDAO.getAppointments("appointments2.csv");

            Thread thread2 = new Thread("Thread2") {
                public void run() {
                    logger.info("run by: " + getName());
                    setAppointments(appointments2);
                }
            };
            thread2.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setAppointments(List<Appointment> appointments) {
        boolean exception = false;
        int i = 0;
        int from = 0;
        int to = 5;
        do {

            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

                int size = appointments.size();

                while (from <= size) {
                    if (to > size) {
                        to = size;
                    }
                    List<Appointment> appointmentListForSend = appointments.subList(from, to);

                    HttpEntity<List<Appointment>> entity = new HttpEntity<List<Appointment>>(appointmentListForSend, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> result = restTemplate.exchange(API_PERSIST, HttpMethod.POST, entity, String.class);


                    if (!result.getStatusCode().equals(HttpStatus.OK)) {
                        break;
                    }
                    from = from + 5;
                    to = to + 5;

                }
            } catch (Exception e) {
                //Exception for status code from http
                exception = true;
                e.printStackTrace();

            }
        } while (!exception);


    }


    private List<Integer> toIdsListFromString(String json) {
        List<Integer> result = new ArrayList<Integer>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
