/*
package com.cdac.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.DTO.UrlRequest;

@RestController
@RequestMapping("/api/keywords")
//@CrossOrigin(origins = "http://localhost:5173") // allow React
public class KeywordController {

    @PostMapping
    public ResponseEntity<List<String>> extractKeywords(@RequestBody UrlRequest request) {
        try {
            Document doc = Jsoup.connect(request.getUrl()).get();

            // Try to extract from <meta name="keywords">
            Elements meta = doc.select("meta[name=keywords]");
            if (!meta.isEmpty()) {
                String content = meta.attr("content");
                List<String> keywords = Arrays.stream(content.split(","))
                                              .map(String::trim)
                                              .collect(Collectors.toList());
                return ResponseEntity.ok(keywords);
            }

            // Fallback: extract frequent words from body text
            String[] words = doc.body().text().split("\\s+");
            Map<String, Long> freq = Arrays.stream(words)
                                           .map(String::toLowerCase)
                                           .filter(w -> w.length() > 4)
                                           .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            List<String> topKeywords = freq.entrySet().stream()
                                           .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                           .limit(10)
                                           .map(Map.Entry::getKey)
                                           .collect(Collectors.toList());

            return ResponseEntity.ok(topKeywords);

        } catch (IOException e) {
            return ResponseEntity.status(400).body(List.of("Error: Invalid URL or site unreachable"));
        }
    }
}
*/

package com.cdac.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cdac.DTO.UrlRequest;

@RestController
@RequestMapping("/api/keywords")
//@CrossOrigin(origins = "http://localhost:5173") 
public class KeywordController {

    private static final Logger logger = LoggerFactory.getLogger(KeywordController.class);

    @PostMapping
    public ResponseEntity<List<String>> extractKeywords(@RequestBody UrlRequest request) {
        String url = request.getUrl();

        // 1. Validate URL format
        if (url == null || !url.startsWith("http")) {
            return ResponseEntity.badRequest().body(List.of("Invalid URL format. Must start with http or https."));
        }

        try {
            // 2. Connect with user-agent and timeout
            Document doc = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 (compatible; SERPSentryBot/1.0)")
                                .timeout(10_000)  // 10 seconds
                                .get();

            // 3. Try to extract from <meta name="keywords">
            Elements meta = doc.select("meta[name=keywords]");
            if (!meta.isEmpty()) {
                String content = meta.attr("content");
                List<String> keywords = Arrays.stream(content.split(","))
                                              .map(String::trim)
                                              .filter(k -> !k.isEmpty())
                                              .collect(Collectors.toList());

                if (!keywords.isEmpty()) {
                    return ResponseEntity.ok(keywords);
                }
            }

            // 4. Fallback: extract frequent words from body text
            String[] words = doc.body().text().split("\\s+");
            Map<String, Long> freq = Arrays.stream(words)
                                           .map(String::toLowerCase)
                                           .filter(w -> w.length() > 4)
                                           .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

            List<String> topKeywords = freq.entrySet().stream()
                                           .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                                           .limit(10)
                                           .map(Map.Entry::getKey)
                                           .collect(Collectors.toList());

            return ResponseEntity.ok(topKeywords);

        } catch (IOException e) {
            logger.error("Failed to scrape keywords from URL: {}", url, e);
            return ResponseEntity.status(400).body(List.of("Error: URL unreachable or invalid HTML structure"));
        } catch (Exception e) {
            logger.error("Unexpected error during keyword extraction", e);
            return ResponseEntity.status(500).body(List.of("Server Error: Please try again later"));
        }
    }
}

