package com.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;

@Entity
@Table(
        name = "lead_assignments",
        uniqueConstraints = @UniqueConstraint(name = "uk_assignment_lead_provider", columnNames = {"lead_id", "provider_id"})
)
public class LeadAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private Instant assignedAt = Instant.now();

    protected LeadAssignment() {
    }

    public LeadAssignment(Lead lead, Provider provider) {
        this.lead = lead;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public Lead getLead() {
        return lead;
    }

    public Provider getProvider() {
        return provider;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }
}
