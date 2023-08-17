package com.notarius.service;

import com.notarius.handler.ShortenerHandler;
import com.notarius.model.DomainPart;
import com.notarius.model.HandledPart;
import com.notarius.repository.DomainPartRepository;
import com.notarius.repository.HandledPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Autowired
    private DomainPartRepository domainPartRepository;

    @Autowired
    private HandledPartRepository handledPartRepository;

    @Override
    public String getOriginalUrl(String shortUrl) {
        String[] parts = ShortenerHandler.splitUrlParts(shortUrl);
        String shortenedPart = parts[1].replaceFirst("/", ""); //the / is taken into consideration when persisting to the db
        if (shortenedPart.length() > 10){
            throw new RuntimeException("Shortened URL handled part length should not be greater than 10 characters");
        }
        Long calculatedId = ShortenerHandler.calculateId(shortenedPart);
        // Check that the handled part exists and linked to the right domain part
        HandledPart handledPart = handledPartRepository.findById(calculatedId).filter(a -> a.getDomainParts().stream().filter(d -> d.getFixedUrl().equals(parts[0])).count() == 1).orElseThrow(()->new RuntimeException("Short URL not found"));

        return parts[0] + handledPart.getOriginalHandledPart();
    }

    @Override
    public String generateShortUrl(String url) {

        String[] parts = ShortenerHandler.splitUrlParts(url);

        return saveUrlAndReturnShortened(parts[0], parts[1]);
    }


    private String saveUrlAndReturnShortened(String domainPart, String originalHandledPart) {

        HandledPart handledPart = handledPartRepository.findHandledPartByOriginalHandledPart(originalHandledPart);
        DomainPart domainPartEntity = domainPartRepository.findDomainPartByFixedUrl(domainPart);
        if (handledPart == null) {
            handledPart = new HandledPart(originalHandledPart);
        } else if (handledPart.getDomainParts().contains(domainPartEntity)) {
            return domainPart + "/" + ShortenerHandler.calculateShortenedPart(handledPart.getId());
        }
        handledPart.getDomainParts().add(domainPartEntity == null ? new DomainPart(domainPart) : domainPartEntity);
        handledPart = handledPartRepository.save(handledPart);

        return domainPart + "/" + ShortenerHandler.calculateShortenedPart(handledPart.getId());

    }
}
