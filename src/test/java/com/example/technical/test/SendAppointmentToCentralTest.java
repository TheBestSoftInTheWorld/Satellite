package com.example.technical.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendAppointmentToCentralTest {
    final String API_PERSIST = "http://localhost:8080/api/setappointments";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void setAppointments() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> result = restTemplate.exchange(API_PERSIST, HttpMethod.GET, entity, String.class);
        assertThat(result.getBody() == null);
    }
}
