package com.quinbay.sbexample.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/health"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status", is("UP")))
                .andExpect(jsonPath("$.message", is("Application is running successfully")))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testGreetingEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/greeting"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.msg", is("Hello Good mrng")));
    }
} 