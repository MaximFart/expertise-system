package com.expertise.commission.consumer;

import com.expertise.commission.service.CommissionService;
import com.expertise.system.common.Application;
import com.expertise.system.common.OutboxEvent;
import com.expertise.system.common.repository.ApplicationRepository;
import com.expertise.system.common.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommissionConsumer {

    private static final String COMMISSION_TOPIC = "commission-events";
    private static final String APPLICATION_TOPIC = "application-events";
    private static final String KAFKA_GROUP_ID = "commission-group";

    private final ApplicationRepository applicationRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final CommissionService commissionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = COMMISSION_TOPIC, groupId = KAFKA_GROUP_ID)
    @Transactional
    public void processCommissionRequest(String message) {
        try {
            Application application = objectMapper.readValue(message, Application.class);
            log.info("Processing application ID: {}", application.getId());

            Application processedApp = commissionService.processCommission(application);
            applicationRepository.save(processedApp);

            // Создание OutboxEvent через конструктор
            OutboxEvent event = new OutboxEvent();
            event.setAggregateType("Application");
            event.setAggregateId(processedApp.getId().toString());
            event.setEventType(APPLICATION_TOPIC);
            event.setPayload(objectMapper.writeValueAsString(processedApp));
            event.setCreatedAt(LocalDateTime.now());
            event.setPublished(false);

            outboxEventRepository.save(event);

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            throw new RuntimeException("Invalid message format", e);
        } catch (Exception e) {
            log.error("Processing error: {}", e.getMessage(), e);
            throw new RuntimeException("Commission processing failed", e);
        }
    }
}