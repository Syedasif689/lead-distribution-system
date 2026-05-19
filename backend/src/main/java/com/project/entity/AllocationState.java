package com.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "allocation_state")
public class AllocationState {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 32)
    private ServiceType serviceType;

    @Column(name = "last_provider_index", nullable = false)
    private int lastProviderIndex;

    public ServiceType getServiceType() {
        return serviceType;
    }

    public int getLastProviderIndex() {
        return lastProviderIndex;
    }

    public void setLastProviderIndex(int lastProviderIndex) {
        this.lastProviderIndex = lastProviderIndex;
    }
}
