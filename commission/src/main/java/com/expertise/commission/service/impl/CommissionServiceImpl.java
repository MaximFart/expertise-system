package com.expertise.commission.service.impl;

import com.expertise.system.common.Application;
import com.expertise.system.common.Application.ApplicationStatus;
import com.expertise.commission.dto.CommissionDecisionDto;
import com.expertise.commission.service.CommissionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommissionServiceImpl implements CommissionService {
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Application processCommission(Application application) {
        // Логика обработки комиссией
        application.setStatus(ApplicationStatus.COMMISSION_REVIEW);
        application.setUpdatedAt(LocalDateTime.now());
        // Здесь может быть сложная логика принятия решений
        // Например, сбор решений от нескольких экспертов

        return application;
    }

    @Override
    @Transactional
    public CommissionDecisionDto makeDecision(String applicationId, String expertId, String decision) {
        // Логика регистрации решения конкретного эксперта
        // Может сохраняться в отдельную таблицу в БД
        return new CommissionDecisionDto(
                UUID.randomUUID().toString(),
                applicationId,
                expertId,
                decision,
                LocalDateTime.now()
        );
    }
}