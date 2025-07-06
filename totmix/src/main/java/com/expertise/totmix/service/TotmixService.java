package com.expertise.totmix.service;

import com.expertise.system.common.Application;
import com.expertise.system.common.OutboxEvent;
import com.expertise.system.common.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TotmixService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public TotmixService(KafkaTemplate<String, String> kafkaTemplate,
                         OutboxEventRepository outboxEventRepository,
                         ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void processApplicationFromPRZ(String applicationJson) {
        try {
            Application application = objectMapper.readValue(applicationJson, Application.class);
            application.setStatus(Application.ApplicationStatus.NEW);

            saveToOutbox("totmix_to_se", application);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process application from PRZ", e);
        }
    }

    @Transactional
    public void processApplicationFromSE(String applicationJson) {
        try {
            Application application = objectMapper.readValue(applicationJson, Application.class);

            if (application.getStatus() == Application.ApplicationStatus.COMMISSION_REVIEW) {
                saveToOutbox("totmix_to_mk", application);
            } else if (application.getStatus() == Application.ApplicationStatus.EXPERTISE_REVIEW) {
                saveToOutbox("totmix_to_me", application);
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process application from SE", e);
        }
    }

    @Transactional
    public void processCommissionResult(String applicationJson) {
        try {
            Application application = objectMapper.readValue(applicationJson, Application.class);

            // После комиссии заявка возвращается в СЭ для дальнейшей обработки
            saveToOutbox("totmix_to_se", application);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process commission result", e);
        }
    }

    @Transactional
    public void processExpertiseResult(String applicationJson) {
        try {
            Application application = objectMapper.readValue(applicationJson, Application.class);

            // После экспертизы заявка возвращается в СЭ
            saveToOutbox("totmix_to_se", application);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to process expertise result", e);
        }
    }

    private void saveToOutbox(String topic, Application application) throws JsonProcessingException {
        OutboxEvent event = new OutboxEvent();
        event.setAggregateType("Application");
        event.setAggregateId(application.getId().toString());
        event.setEventType(topic);
        event.setPayload(objectMapper.writeValueAsString(application));
        event.setCreatedAt(LocalDateTime.now());

        outboxEventRepository.save(event);
    }
}
