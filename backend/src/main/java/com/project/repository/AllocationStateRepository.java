package com.project.repository;

import com.project.entity.AllocationState;
import com.project.entity.ServiceType;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AllocationStateRepository extends JpaRepository<AllocationState, ServiceType> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AllocationState a where a.serviceType = :serviceType")
    Optional<AllocationState> lockByServiceType(@Param("serviceType") ServiceType serviceType);
}
