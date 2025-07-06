package com.expertise.system.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic incomingApplicationsTopic() {
        return TopicBuilder.name("system.applications.incoming")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic commissionTopic() {
        return TopicBuilder.name("commission-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}