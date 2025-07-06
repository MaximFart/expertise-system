package com.expertise.commission.repository;

import com.expertise.system.common.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface CommissionRepository extends JpaRepository<Application, UUID> {
    // Дополнительные методы запросов специфичные для комиссии
}