package com.notarius.controller;

import com.notarius.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/originalurl")
    public ResponseEntity<String> getOriginalUrl(@RequestBody String shortUrl) {

        String originalUrl = "";
        try {
            originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return new ResponseEntity<>(originalUrl,HttpStatus.OK);
    }

    @PostMapping("/shorturl")
    public ResponseEntity<String> generateShortUrl(@RequestBody String originalUrl) {
        String shortUrl = "";
        try {
            shortUrl = urlShortenerService.generateShortUrl(originalUrl);
        } catch (RuntimeException e){
             return ResponseEntity.badRequest().body(e.getMessage());
        }

        return new ResponseEntity<>(shortUrl,HttpStatus.OK);
    }

}
