package com.expertise.commission.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommissionDecisionDto {
    private String id;
    private String applicationId;
    private String expertId;
    private String decision;
    private LocalDateTime decisionDate;

    public CommissionDecisionDto(String id, String applicationId,
                                 String expertId, String decision,
                                 LocalDateTime decisionDate) {
        this.id = id;
        this.applicationId = applicationId;
        this.expertId = expertId;
        this.decision = decision;
        this.decisionDate = decisionDate;
    }
}