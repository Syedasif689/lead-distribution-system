package com.project.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WebhookResetQuotaRequest(
        @JsonProperty("event_id")
        @JsonAlias("event_id")
        @NotBlank
        @Size(max = 120)
        String eventId
) {
}
