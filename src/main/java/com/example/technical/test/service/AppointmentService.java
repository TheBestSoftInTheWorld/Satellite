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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


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
        logger.info("checking for new appointments");
        List<Appointment> appointments = iAppointmentDAO.getAppointments("appointments.csv");
        List<Appointment> appointments2 = iAppointmentDAO.getAppointments("appointments2.csv");

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Runnable task1 = () -> {
            logger.info("Executing Task1 inside : " + Thread.currentThread().getName());
            setAppointments(appointments);
        };

        Runnable task2 = () -> {
            logger.info("Executing Task2 inside : " + Thread.currentThread().getName());
            setAppointments(appointments2);

        };
        executorService.submit(task1);
        executorService.submit(task2);
    }

    @Override
    public void setAppointments(List<Appointment> appointments) {
        List<Appointment> appointmentsForSent;
        do {
            // filter(appointment -> appointment.getModified().before(new Date())
            // it imitates the action of the emergence of new appointments
            appointmentsForSent = appointments.stream().filter(appointment -> !appointment.isSend())
                    .filter(appointment -> appointment.getModified().before(new Date()))
                    .limit(5).collect(Collectors.toList());
            if (appointmentsForSent.isEmpty()) {
                logger.info("List of appointments for sent is empty");

            } else {
                logger.info("sending data about appointments");

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    HttpEntity<List<Appointment>> entity = new HttpEntity<List<Appointment>>(appointmentsForSent, headers);
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> result = restTemplate.exchange(API_PERSIST, HttpMethod.POST, entity, String.class);
                    changeAppointmentsForSent(result.getBody(), appointments);
                    if (result.getStatusCode().equals(HttpStatus.OK)) {
                        logger.info("status code 200 for sent appointments");
                    }
                } catch (HttpStatusCodeException exception) {
                    //Exception for status code from http
                    logger.info("Exception for sent appointments, status code: " + exception.getStatusCode().value());
                    exception.printStackTrace();
                } catch (Exception e) {
                    //Exception for status code from http
                    logger.info("Exception for sent appointments");
                    e.printStackTrace();
                }
            }
        }
        while (!appointmentsForSent.isEmpty());


    }

    private void changeAppointmentsForSent(String ids, List<Appointment> appointmentsSent) {
        List<Integer> idsList = toIdsListFromString(ids);
        for (int i = 0; i < idsList.size(); i++) {
            long remoteAppointmentId = idsList.get(i).longValue();
            appointmentsSent.stream().filter(appointment -> appointment.getRemoteAppointmentId() == remoteAppointmentId).forEach(foundAppointment -> foundAppointment.setSend(true));
        }
    }

    private List<Integer> toIdsListFromString(String json) {
        List<Integer> result = new ArrayList<Integer>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.readValue(json, List.class);
        } catch (Exception e) {
        }

        return result;
    }

    @Override
    public void runEvery30Seconds() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            setAppointments();
        };
        executor.scheduleWithFixedDelay(task, 0, 30000, TimeUnit.MILLISECONDS);
    }

}
