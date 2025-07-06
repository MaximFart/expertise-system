package com.expertise.system.service;

import com.expertise.system.common.Application;
import com.expertise.system.common.OutboxEvent;
import com.expertise.system.common.repository.ApplicationRepository;
import com.expertise.system.common.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final OutboxEventRepository outboxEventRepository;


    @Transactional
    public void processApplication(Application application) {
        // 1. Сохраняем заявку
        applicationRepository.save(application);

        // 2. Определяем топик для обработки
        String topic = switch (application.getStatus()) {
            case COMMISSION_REVIEW -> "commission-events";
            case EXPERTISE_REVIEW -> "expertise-events";
            default -> throw new IllegalArgumentException("Unsupported status: " + application.getStatus());
        };

        // 3. Создаем и сохраняем событие
        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("Application");
        event.setAggregateId(application.getId().toString());
        event.setEventType(topic);
        event.setPayload(applicationToJson(application));
        event.setCreatedAt(java.time.LocalDateTime.now());

        outboxEventRepository.save(event);
    }

    private String applicationToJson(Application app) {
        return String.format(
                "{\"id\":\"%s\",\"status\":\"%s\",\"number\":\"%s\"}",
                app.getId(),
                app.getStatus(),
                app.getNumber()
        );
    }
}