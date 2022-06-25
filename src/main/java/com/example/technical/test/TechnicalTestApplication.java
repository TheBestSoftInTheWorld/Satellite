package com.example.technical.test;

import com.example.technical.test.service.AppointmentService;
import com.example.technical.test.service.IAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TechnicalTestApplication {
    @Autowired
    IAppointmentService iAppointmentService;

    public static void main(String[] args) {
        SpringApplication.run(TechnicalTestApplication.class, args);
    }

    @Bean
    public CommandLineRunner CommandLineRunnerBean() {
        return (args) -> {
            iAppointmentService.runEvery30Seconds();
        };
    }
}
