package com.expertise.portal.controller;


import com.expertise.portal.response.ApplicationResponse;
import com.expertise.portal.service.ApplicationService;
import com.expertise.system.common.Application;
import com.expertise.system.common.OutboxEvent;
import com.expertise.system.common.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/applications")
@EnableAsync
public class ApplicationController {

    private final ApplicationService applicationService;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    public ApplicationController(ApplicationService applicationService,
                                 OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper) {
        this.applicationService = applicationService;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Async
    public CompletableFuture<ResponseEntity<ApplicationResponse>> createApplicationAsync(
            @RequestBody Application application) {

        try {
            Application processedApplication = applicationService.processApplication(application);

            // Создание записи в outbox
            OutboxEvent outboxEvent = new OutboxEvent();
            outboxEvent.setAggregateType("Application");
            outboxEvent.setAggregateId(processedApplication.getId().toString());
            outboxEvent.setEventType("prz_to_totmix");
            outboxEvent.setPayload(objectMapper.writeValueAsString(processedApplication));
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxEvent.setPublished(false);

            outboxEventRepository.save(outboxEvent);

            ApplicationResponse response = new ApplicationResponse(
                    "Заявка успешно принята в обработку",
                    processedApplication.getId(),
                    processedApplication.getStatus());

            return CompletableFuture.completedFuture(ResponseEntity.ok(response));

        } catch (Exception e) {
            ApplicationResponse errorResponse = new ApplicationResponse(
                    "Ошибка при обработке заявки: " + e.getMessage(),
                    null,
                    null);

            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
        }
    }

}