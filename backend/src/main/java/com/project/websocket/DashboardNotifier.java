package com.project.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class DashboardNotifier {
    private final SimpMessagingTemplate messagingTemplate;

    public DashboardNotifier(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishDashboardRefresh() {
        messagingTemplate.convertAndSend("/topic/assignments", new DashboardEvent("ASSIGNMENTS_UPDATED"));
    }

    public void publishDashboardRefreshAfterCommit() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            publishDashboardRefresh();
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publishDashboardRefresh();
            }
        });
    }

    public record DashboardEvent(String type) {
    }
}
