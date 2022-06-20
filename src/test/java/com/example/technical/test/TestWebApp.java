package com.example.technical.test;

import com.example.technical.test.controller.APIController;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestWebApp {

    @Autowired
    private APIController apiController;


    @Test
    public void contextLoads() throws Exception {
        assertThat(apiController).isNotNull();
    }
}
