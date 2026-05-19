package com.project.service;

import com.project.entity.ServiceType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AllocationPolicy {
    private static final Map<ServiceType, List<Long>> MANDATORY = Map.of(
            ServiceType.SERVICE_1, List.of(1L),
            ServiceType.SERVICE_2, List.of(5L),
            ServiceType.SERVICE_3, List.of(1L, 4L)
    );

    private static final Map<ServiceType, List<Long>> ROUND_ROBIN = Map.of(
            ServiceType.SERVICE_1, List.of(2L, 3L, 4L),
            ServiceType.SERVICE_2, List.of(6L, 7L, 8L),
            ServiceType.SERVICE_3, List.of(2L, 3L, 5L, 6L, 7L, 8L)
    );

    public List<Long> mandatoryProviderIds(ServiceType serviceType) {
        return MANDATORY.get(serviceType);
    }

    public List<Long> roundRobinProviderIds(ServiceType serviceType) {
        return ROUND_ROBIN.get(serviceType);
    }

    public int totalRequiredAssignments(ServiceType serviceType) {
        return 3;
    }
}
