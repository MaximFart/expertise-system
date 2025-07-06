package com.expertise.system.common;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public static final String APPLICATION_TOPIC = "application-events";
    public static final String COMMISSION_TOPIC = "commission-events";
    public static final String EXPERTISE_TOPIC = "expertise-events";
    public static final String PORTAL_TOPIC = "portal-events";

    @Bean
    public NewTopic applicationTopic() {
        return TopicBuilder.name(APPLICATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic commissionTopic() {
        return TopicBuilder.name(COMMISSION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic expertiseTopic() {
        return TopicBuilder.name(EXPERTISE_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic portalTopic() {
        return TopicBuilder.name(PORTAL_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}