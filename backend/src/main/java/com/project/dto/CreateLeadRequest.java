package com.project.dto;

import com.project.entity.ServiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateLeadRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 32) String phone,
        @NotBlank @Size(max = 120) String city,
        @NotNull ServiceType serviceType,
        @NotBlank @Size(max = 2000) String description
) {
}
