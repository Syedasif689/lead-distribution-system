package com.project.controller;

import com.project.dto.CreateLeadRequest;
import com.project.dto.LeadResponse;
import com.project.service.LeadService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leads")
public class LeadController {
    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
        LeadResponse response = leadService.createLead(request);
        return ResponseEntity.created(URI.create("/api/leads/" + response.id())).body(response);
    }

    @GetMapping
    public List<LeadResponse> getLeads() {
        return leadService.getLeads();
    }
}
