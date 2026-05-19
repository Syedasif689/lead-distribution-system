package com.project.service;

import com.project.dto.AssignedLeadResponse;
import com.project.dto.ProviderResponse;
import com.project.repository.LeadAssignmentRepository;
import com.project.repository.ProviderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;
    private final LeadAssignmentRepository leadAssignmentRepository;

    public ProviderService(ProviderRepository providerRepository, LeadAssignmentRepository leadAssignmentRepository) {
        this.providerRepository = providerRepository;
        this.leadAssignmentRepository = leadAssignmentRepository;
    }

    @Transactional(readOnly = true)
    public List<ProviderResponse> getProviders() {
        return providerRepository.findAllByOrderByIdAsc().stream()
                .map(provider -> {
                    var assignedLeads = leadAssignmentRepository.findByProviderIdOrderByAssignedAtDesc(provider.getId()).stream()
                            .map(AssignedLeadResponse::from)
                            .toList();
                    return ProviderResponse.of(provider, assignedLeads);
                })
                .toList();
    }
}
