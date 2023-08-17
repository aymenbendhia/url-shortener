package com.notarius.service;

public interface UrlShortenerService {
    String getOriginalUrl(String shortUrl);

    String generateShortUrl(String originalUrl);
}
