package com.project.dto;

import com.project.entity.LeadAssignment;
import com.project.entity.ServiceType;
import java.time.Instant;

public record AssignedLeadResponse(
        Long assignmentId,
        Long leadId,
        String customerName,
        String phone,
        String city,
        ServiceType serviceType,
        String description,
        Instant assignedAt
) {
    public static AssignedLeadResponse from(LeadAssignment assignment) {
        var lead = assignment.getLead();
        return new AssignedLeadResponse(
                assignment.getId(),
                lead.getId(),
                lead.getName(),
                lead.getPhone(),
                lead.getCity(),
                lead.getServiceType(),
                lead.getDescription(),
                assignment.getAssignedAt()
        );
    }
}
