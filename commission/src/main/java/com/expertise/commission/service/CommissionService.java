package com.expertise.commission.service;

import com.expertise.system.common.Application;
import com.expertise.commission.dto.CommissionDecisionDto;

public interface CommissionService {
    Application processCommission(Application application);
    CommissionDecisionDto makeDecision(String applicationId, String expertId, String decision);
}