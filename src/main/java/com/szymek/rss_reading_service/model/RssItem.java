package com.szymek.rss_reading_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RssItem {
    private String title;
    private String link;
    private String description;
    private String author;
    private Instant publishedDate;
    private String sourceUrl;
}