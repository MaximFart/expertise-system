package com.expertise.totmix.consumer;

import com.expertise.totmix.service.TotmixService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TotmixListeners {

    private final TotmixService service;

    public TotmixListeners(TotmixService totmixService) {
        this.service = totmixService;
    }

    @KafkaListener(topics = "prz_to_totmix", groupId = "totmix-group")
    public void listenFromPRZ(String applicationJson) {
        service.processApplicationFromPRZ(applicationJson);
    }

    @KafkaListener(topics = "se_to_totmix", groupId = "totmix-group")
    public void listenFromSE(String applicationJson) {
        service.processApplicationFromSE(applicationJson);
    }

    @KafkaListener(topics = "mk_to_totmix", groupId = "totmix-group")
    public void listenFromMK(String applicationJson) {
        service.processCommissionResult(applicationJson);
    }

    @KafkaListener(topics = "me_to_totmix", groupId = "totmix-group")
    public void listenFromME(String applicationJson) {
        service.processExpertiseResult(applicationJson);
    }
}
