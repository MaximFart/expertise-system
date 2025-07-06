package com.expertise.system.common;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic przToTotmixTopic() {
        return TopicBuilder.name("prz_to_totmix")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic seToTotmixTopic() {
        return TopicBuilder.name("se_to_totmix")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic totmixToMkTopic() {
        return TopicBuilder.name("totmix_to_mk")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic mkToTotmixTopic() {
        return TopicBuilder.name("mk_to_totmix")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic totmixToMeTopic() {
        return TopicBuilder.name("totmix_to_me")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic meToTotmixTopic() {
        return TopicBuilder.name("me_to_totmix")
                .partitions(3)
                .replicas(2)
                .build();
    }

    @Bean
    public NewTopic totmixToSeTopic() {
        return TopicBuilder.name("totmix_to_se")
                .partitions(3)
                .replicas(2)
                .build();
    }
}