package com.project.service;

import com.project.entity.AllocationState;
import com.project.entity.Lead;
import com.project.entity.LeadAssignment;
import com.project.entity.Provider;
import com.project.exception.QuotaUnavailableException;
import com.project.repository.AllocationStateRepository;
import com.project.repository.LeadAssignmentRepository;
import com.project.repository.ProviderRepository;
import com.project.websocket.DashboardNotifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationService {
    private final AllocationPolicy allocationPolicy;
    private final AllocationStateRepository allocationStateRepository;
    private final ProviderRepository providerRepository;
    private final LeadAssignmentRepository leadAssignmentRepository;
    private final DashboardNotifier dashboardNotifier;

    public AllocationService(
            AllocationPolicy allocationPolicy,
            AllocationStateRepository allocationStateRepository,
            ProviderRepository providerRepository,
            LeadAssignmentRepository leadAssignmentRepository,
            DashboardNotifier dashboardNotifier
    ) {
        this.allocationPolicy = allocationPolicy;
        this.allocationStateRepository = allocationStateRepository;
        this.providerRepository = providerRepository;
        this.leadAssignmentRepository = leadAssignmentRepository;
        this.dashboardNotifier = dashboardNotifier;
    }

    @Transactional
    public List<LeadAssignment> allocate(Lead lead) {
        AllocationState state = allocationStateRepository.lockByServiceType(lead.getServiceType())
                .orElseThrow(() -> new IllegalStateException("Missing allocation state for " + lead.getServiceType()));

        List<Long> mandatoryIds = allocationPolicy.mandatoryProviderIds(lead.getServiceType());
        List<Long> poolIds = allocationPolicy.roundRobinProviderIds(lead.getServiceType());
        LinkedHashSet<Long> lockIds = Stream.concat(mandatoryIds.stream(), poolIds.stream())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        Map<Long, Provider> providersById = providerRepository.lockAllByIdIn(lockIds).stream()
                .collect(Collectors.toMap(Provider::getId, Function.identity()));

        List<Provider> selected = new ArrayList<>();
        for (Long providerId : mandatoryIds) {
            Provider provider = providersById.get(providerId);
            if (provider == null || !provider.hasQuota()) {
                throw new QuotaUnavailableException("Mandatory provider " + providerId + " has no available quota");
            }
            selected.add(provider);
        }

        int neededFromPool = allocationPolicy.totalRequiredAssignments(lead.getServiceType()) - selected.size();
        int nextIndex = selectRoundRobinProviders(state, poolIds, providersById, selected, neededFromPool);
        state.setLastProviderIndex(nextIndex);

        List<LeadAssignment> assignments = selected.stream()
                .map(provider -> {
                    provider.incrementUsedQuota();
                    return new LeadAssignment(lead, provider);
                })
                .toList();

        List<LeadAssignment> saved = leadAssignmentRepository.saveAll(assignments);
        dashboardNotifier.publishDashboardRefreshAfterCommit();
        return saved;
    }

    private int selectRoundRobinProviders(
            AllocationState state,
            List<Long> poolIds,
            Map<Long, Provider> providersById,
            List<Provider> selected,
            int neededFromPool
    ) {
        int selectedFromPool = 0;
        int currentIndex = state.getLastProviderIndex();
        int attempts = 0;

        while (selectedFromPool < neededFromPool && attempts < poolIds.size()) {
            currentIndex = (currentIndex + 1) % poolIds.size();
            Provider candidate = providersById.get(poolIds.get(currentIndex));

            if (candidate != null && candidate.hasQuota() && selected.stream().noneMatch(p -> p.getId().equals(candidate.getId()))) {
                selected.add(candidate);
                selectedFromPool++;
            }
            attempts++;
        }

        if (selectedFromPool < neededFromPool) {
            throw new QuotaUnavailableException("Not enough provider quota available for fair allocation");
        }

        return currentIndex;
    }
}
