package com.project.service;

import com.project.dto.CreateLeadRequest;
import com.project.dto.GenerateLeadsResponse;
import com.project.dto.LeadResponse;
import com.project.entity.ServiceType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    private final LeadService leadService;
    private final Executor executor;

    public TestService(LeadService leadService, @Qualifier("applicationTaskExecutor") Executor executor) {
        this.leadService = leadService;
        this.executor = executor;
    }

    public GenerateLeadsResponse generateLeads() {
        String batch = UUID.randomUUID().toString().substring(0, 8);
        List<CompletableFuture<LeadResponse>> futures = java.util.stream.IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> leadService.createLead(new CreateLeadRequest(
                        "Generated Customer " + i,
                        "90000" + batch.substring(0, 4) + i,
                        "Mumbai",
                        ServiceType.values()[i % ServiceType.values().length],
                        "Generated test lead " + i + " for concurrency verification"
                )), executor))
                .toList();

        List<LeadResponse> leads = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        return new GenerateLeadsResponse(10, leads.size(), leads);
    }
}
