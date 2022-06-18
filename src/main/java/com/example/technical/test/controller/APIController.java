package com.example.technical.test.controller;

import com.example.technical.test.model.Appointment;
import com.example.technical.test.model.StateEnum;
import com.example.technical.test.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("api/")
public class APIController {
     final String API_PERSIST="http://localhost:8081/api/persistAppointments";
    @Autowired
    IAppointmentService iAppointmentService;

    @RequestMapping(method = RequestMethod.GET, value = "fulllist")
    @ResponseBody
    public ResponseEntity<List<Appointment>> appointments() {
        Date now = new Date();
        List<Appointment> result = iAppointmentService.getAppointments(now);
        return new ResponseEntity<List<Appointment>>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "setappointments")
    @ResponseBody
    public ResponseEntity<String> setAppointments() {
        Date now = new Date();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        List<Appointment> appointmentList = iAppointmentService.getAppointments(now);
        int size = appointmentList.size();

        int i = 0;
        int from = 0;
        int to = 5;
        while (from <= size) {
            if (to > size) {
                to = size;
            }
            List<Appointment> appointmentListForSend = appointmentList.subList(from, to);

            HttpEntity<List<Appointment>> entity = new HttpEntity<List<Appointment>>(appointmentListForSend, headers);
            RestTemplate restTemplate = new RestTemplate();
            HttpStatus result = restTemplate.exchange(API_PERSIST, HttpMethod.POST, entity, String.class).getStatusCode();
            if (!result.equals(HttpStatus.OK)) {
                break;
            }
            from = from + 5;
            to = to + 5;

        }


        return new ResponseEntity<String>("result", HttpStatus.OK);
    }


}
