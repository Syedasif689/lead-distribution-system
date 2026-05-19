package com.project.dto;

import com.project.entity.Lead;
import com.project.entity.ServiceType;
import java.time.Instant;

public record LeadResponse(
        Long id,
        String name,
        String phone,
        String city,
        ServiceType serviceType,
        String description,
        Instant createdAt
) {
    public static LeadResponse from(Lead lead) {
        return new LeadResponse(
                lead.getId(),
                lead.getName(),
                lead.getPhone(),
                lead.getCity(),
                lead.getServiceType(),
                lead.getDescription(),
                lead.getCreatedAt()
        );
    }
}
