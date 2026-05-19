package com.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "providers")
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(name = "monthly_quota", nullable = false)
    private int monthlyQuota;

    @Column(name = "used_quota", nullable = false)
    private int usedQuota;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMonthlyQuota() {
        return monthlyQuota;
    }

    public int getUsedQuota() {
        return usedQuota;
    }

    public int getRemainingQuota() {
        return monthlyQuota - usedQuota;
    }

    public boolean hasQuota() {
        return usedQuota < monthlyQuota;
    }

    public void incrementUsedQuota() {
        if (!hasQuota()) {
            throw new IllegalStateException("Provider quota exhausted");
        }
        usedQuota++;
    }

    public void resetUsedQuota() {
        usedQuota = 0;
    }
}
