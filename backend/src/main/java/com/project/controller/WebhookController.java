package com.project.controller;

import com.project.dto.WebhookResetQuotaRequest;
import com.project.dto.WebhookResponse;
import com.project.service.WebhookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {
    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping("/reset-quota")
    public WebhookResponse resetQuota(@Valid @RequestBody WebhookResetQuotaRequest request) {
        return webhookService.resetQuota(request.eventId());
    }
}
