package com.expertise.system.common;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxScheduler {
    private final EntityManager entityManager;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 120000) // 2 минуты
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> events = entityManager
                .createQuery("SELECT e FROM OutboxEvent e WHERE e.published = false ORDER BY e.createdAt", OutboxEvent.class)
                .getResultList();

        if (events.isEmpty()) {
            return;
        }

        log.info("Найдено {} неотправленных событий", events.size());

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getPayload());
                event.setPublished(true);
                entityManager.merge(event);
                log.debug("Отправлено событие: {}", event);
            } catch (Exception e) {
                log.error("Ошибка отправки события: {}", event, e);
            }
        }

        entityManager.flush();
    }
}
