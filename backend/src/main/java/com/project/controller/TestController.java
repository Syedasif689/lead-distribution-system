package com.project.controller;

import com.project.dto.GenerateLeadsResponse;
import com.project.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/generate-leads")
    public GenerateLeadsResponse generateLeads() {
        return testService.generateLeads();
    }
}
