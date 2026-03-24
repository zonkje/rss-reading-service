package com.szymek.rss_reading_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

//TODO: consider Avro or shared lib

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RssItemDto {
    private String title;
    private String link;
    private String description;
    private String author;
    private Instant publishedDate;
    private String source;
}