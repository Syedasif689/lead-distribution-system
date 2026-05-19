package com.project.dto;

public record WebhookResponse(String eventId, boolean processed, String message) {
}
