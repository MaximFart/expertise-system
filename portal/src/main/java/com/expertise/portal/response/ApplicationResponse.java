package com.expertise.portal.response;

import com.expertise.system.common.Application;

import java.util.UUID;

public class ApplicationResponse {
    private String message;
    private UUID applicationId;
    private Application.ApplicationStatus status;

    public ApplicationResponse(String message, UUID applicationId, Application.ApplicationStatus status) {
        this.message = message;
        this.applicationId = applicationId;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(UUID applicationId) {
        this.applicationId = applicationId;
    }

    public Application.ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(Application.ApplicationStatus status) {
        this.status = status;
    }
}
