package com.project.repository;

import com.project.entity.WebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebhookEventRepository extends JpaRepository<WebhookEvent, String> {
    @Modifying
    @Query(value = """
            insert into webhook_events (event_id, processed_at)
            values (:eventId, now())
            on conflict (event_id) do nothing
            """, nativeQuery = true)
    int insertIfAbsent(@Param("eventId") String eventId);
}
