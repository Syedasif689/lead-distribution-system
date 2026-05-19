package com.project.service;

import com.project.dto.CreateLeadRequest;
import com.project.dto.LeadResponse;
import com.project.entity.Lead;
import com.project.exception.DuplicateLeadException;
import com.project.repository.LeadAssignmentRepository;
import com.project.repository.LeadRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeadService {
    private final LeadRepository leadRepository;
    private final LeadAssignmentRepository leadAssignmentRepository;
    private final AllocationService allocationService;

    public LeadService(
            LeadRepository leadRepository,
            LeadAssignmentRepository leadAssignmentRepository,
            AllocationService allocationService
    ) {
        this.leadRepository = leadRepository;
        this.leadAssignmentRepository = leadAssignmentRepository;
        this.allocationService = allocationService;
    }

    @Transactional
    public LeadResponse createLead(CreateLeadRequest request) {
        Lead lead = new Lead();
        lead.setName(request.name().trim());
        lead.setPhone(request.phone().trim());
        lead.setCity(request.city().trim());
        lead.setServiceType(request.serviceType());
        lead.setDescription(request.description().trim());

        try {
            Lead saved = leadRepository.saveAndFlush(lead);
            allocationService.allocate(saved);
            long assignmentCount = leadAssignmentRepository.countByLeadId(saved.getId());
            if (assignmentCount != 3) {
                throw new IllegalStateException("Lead must have exactly 3 assignments");
            }
            return LeadResponse.from(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateLeadException();
        }
    }

    @Transactional(readOnly = true)
    public List<LeadResponse> getLeads() {
        return leadRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(LeadResponse::from)
                .toList();
    }
}
