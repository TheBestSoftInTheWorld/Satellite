package com.example.technical.test.controller;

import com.example.technical.test.dao.IAppointmentDAO;
import com.example.technical.test.model.Appointment;
import com.example.technical.test.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("api/")
public class APIController {
    @Autowired
    IAppointmentService iAppointmentService;

    @RequestMapping(method = RequestMethod.GET, value = "fulllist")
    @ResponseBody
    public ResponseEntity<List<Appointment>> appointments() {
        Date now = new Date();
        List<Appointment> result = iAppointmentService.getAppointments(now);
        return new ResponseEntity<List<Appointment>>(result, HttpStatus.OK);
    }
}
