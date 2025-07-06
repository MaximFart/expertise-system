package com.expertise.system.consumer;

import com.expertise.system.common.Application;
import com.expertise.system.common.OutboxEvent;
import com.expertise.system.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationConsumer {

    private final ApplicationService applicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "system.applications.incoming")
    @Transactional
    public void processIncomingApplication(String message) {
        try {
            Application application = objectMapper.readValue(message, Application.class);
            log.info("Received application ID: {}", application.getId());

            applicationService.processApplication(application);

        } catch (Exception e) {
            log.error("Failed to process application: {}", e.getMessage(), e);
        }
    }
}