package com.szymek.rss_reading_service.kafka;

import com.szymek.rss_reading_service.dto.RssItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssItemProducer {

    //TODO: topic name to properties file
    private static final String TOPIC = "rss-items";
    private final KafkaTemplate<String, RssItemDto> kafkaTemplate;

    public void publish(RssItemDto dto) {
        kafkaTemplate.send(TOPIC, dto.getLink(), dto)
                .whenComplete((result, ex) -> {
                    //TODO: custom exception, exception handling
                    if (ex != null) {
                        log.error("Failed to send item: {}", dto.getTitle(), ex);
                    } else {
                        log.info("Sent: {} | offset: {}", dto.getTitle(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}