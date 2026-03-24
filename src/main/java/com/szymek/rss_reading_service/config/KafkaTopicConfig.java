package com.szymek.rss_reading_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic rssItemsTopic() {
        //TODO: topic name to properties file
        return TopicBuilder.name("rss-items")
                .partitions(3)
                .replicas(1)
                .build();
    }
}