package com.project.repository;

import com.project.entity.LeadAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadAssignmentRepository extends JpaRepository<LeadAssignment, Long> {
    @EntityGraph(attributePaths = {"lead", "provider"})
    List<LeadAssignment> findByProviderIdOrderByAssignedAtDesc(Long providerId);

    long countByLeadId(Long leadId);
}
