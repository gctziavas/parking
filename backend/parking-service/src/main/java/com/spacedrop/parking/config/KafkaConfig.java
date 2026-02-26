package com.spacedrop.parking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String PARKING_EVENTS_TOPIC = "parking-events";

    @Bean
    public NewTopic parkingEventsTopic() {
        return TopicBuilder.name(PARKING_EVENTS_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
