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

import java.util.List;


@Controller
@RequestMapping("api/")
public class APIController {

    @Autowired
    IAppointmentService iAppointmentService;

    @RequestMapping(method = RequestMethod.GET, value = "fulllist")
    @ResponseBody
    public ResponseEntity<List<Appointment>> appointments() {

        List<Appointment> result = iAppointmentService.getAppointments("appointments.csv");
        return new ResponseEntity<List<Appointment>>(result, HttpStatus.OK);
    }


}
