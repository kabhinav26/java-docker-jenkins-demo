package com.quinbay.sbexample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Application is running successfully");
        return response;
    }

    @GetMapping("/greeting")
    public Map<String, String> greeting() {
        Map<String, String> response = new HashMap<>();
        response.put("msg", "Hello Good mrng");
        return response;
    }
} 