package com.example.technical.test.controller;

import com.example.technical.test.model.Appointment;
import com.example.technical.test.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("api/")
public class APIController {

    @Autowired
    IAppointmentService iAppointmentService;

    @RequestMapping(method = RequestMethod.GET, value = "fulllist")
    @ResponseBody
    public ResponseEntity<List<Appointment>> appointments() {

        List<Appointment> result = iAppointmentService.getAppointments();
        return new ResponseEntity<List<Appointment>>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "generateAppointments")
    @ResponseBody
    public ResponseEntity<List<Appointment>> generateAppointments(@RequestParam("size") Integer limit) {
        List<Appointment> result = iAppointmentService.generateAppointments(limit);
        return new ResponseEntity<List<Appointment>>(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "setappointments")
    @ResponseBody
    public ResponseEntity setAppointments() {
        iAppointmentService.setAppointments(iAppointmentService.getAppointments());
        iAppointmentService.setRandomAppointments();
        return new ResponseEntity(HttpStatus.OK);
    }


}
