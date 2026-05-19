package com.project.dto;

import com.project.entity.Provider;
import java.util.List;

public record ProviderResponse(
        Long id,
        String name,
        int monthlyQuota,
        int usedQuota,
        int remainingQuota,
        long leadsReceivedCount,
        List<AssignedLeadResponse> assignedLeads
) {
    public static ProviderResponse of(Provider provider, List<AssignedLeadResponse> assignedLeads) {
        return new ProviderResponse(
                provider.getId(),
                provider.getName(),
                provider.getMonthlyQuota(),
                provider.getUsedQuota(),
                provider.getRemainingQuota(),
                assignedLeads.size(),
                assignedLeads
        );
    }
}
