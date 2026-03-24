package com.szymek.rss_reading_service.service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import com.szymek.rss_reading_service.dto.RssItemDto;
import com.szymek.rss_reading_service.kafka.RssItemProducer;
import com.szymek.rss_reading_service.model.RssItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class RssFetcherService {

    //TODO: find better place to store the URL
    private static final String RSS_URL = "https://openai.com/news/rss.xml";
    private final RssItemProducer producer;

    @Scheduled(fixedDelayString = "${rss.fetch.delay-ms:60000}")
    public void fetchAndPublish() {
        log.info("Fetching RSS feed from: {}", RSS_URL);
        try {
            SyndFeed feed = new SyndFeedInput()
                    //TODO: deprecated, change it
                    .build(new XmlReader(new URL(RSS_URL)));

            feed.getEntries().stream()
                    .map(this::toRssItem)
                    .map(this::toDto)
                    .forEach(producer::publish);

            log.info("Published {} items from RSS", feed.getEntries().size());
        } catch (Exception e) {
            //TODO: fix handling here
            log.error("RSS fetch failed", e);
        }
    }

    //TODO: Add mapper class (maybe use lib like mapstruct?)
    private RssItem toRssItem(SyndEntry entry) {
        return RssItem.builder()
                .title(entry.getTitle())
                .link(entry.getLink())
                .description(entry.getDescription() != null
                        ? entry.getDescription().getValue() : "")
                .author(entry.getAuthor())
                .publishedDate(entry.getPublishedDate() != null
                        ? entry.getPublishedDate().toInstant() : Instant.now())
                .sourceUrl(RSS_URL)
                .build();
    }

    private RssItemDto toDto(RssItem item) {
        return RssItemDto.builder()
                .title(item.getTitle())
                .link(item.getLink())
                .description(item.getDescription())
                .author(item.getAuthor())
                .publishedDate(item.getPublishedDate())
                .source("openai")
                .build();
    }
}