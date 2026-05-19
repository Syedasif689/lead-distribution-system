package com.project.service;

import com.project.dto.WebhookResponse;
import com.project.repository.ProviderRepository;
import com.project.repository.WebhookEventRepository;
import com.project.websocket.DashboardNotifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebhookService {
    private final WebhookEventRepository webhookEventRepository;
    private final ProviderRepository providerRepository;
    private final DashboardNotifier dashboardNotifier;

    public WebhookService(
            WebhookEventRepository webhookEventRepository,
            ProviderRepository providerRepository,
            DashboardNotifier dashboardNotifier
    ) {
        this.webhookEventRepository = webhookEventRepository;
        this.providerRepository = providerRepository;
        this.dashboardNotifier = dashboardNotifier;
    }

    @Transactional
    public WebhookResponse resetQuota(String eventId) {
        int inserted = webhookEventRepository.insertIfAbsent(eventId);
        if (inserted == 0) {
            return new WebhookResponse(eventId, false, "Event already processed");
        }

        providerRepository.lockAllProviders().forEach(provider -> provider.resetUsedQuota());
        dashboardNotifier.publishDashboardRefreshAfterCommit();
        return new WebhookResponse(eventId, true, "Provider quotas reset");
    }
}
