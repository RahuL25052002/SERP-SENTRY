package com.cdac.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.service.KeywordSuggestionService;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/suggestions")
public class KeywordSuggestionController {

    @Autowired
    private KeywordSuggestionService keywordSuggestionService;

    @PostMapping
    public String suggestBetterKeywords(@RequestBody KeywordSuggestionRequest request) {
        return keywordSuggestionService.getBetterKeywords(request.getKeywords(), request.getDomainUrl());
    }

    public static class KeywordSuggestionRequest {
        private List<String> keywords;
        private String domainUrl;

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public String getDomainUrl() {
            return domainUrl;
        }

        public void setDomainUrl(String domainUrl) {
            this.domainUrl = domainUrl;
        }
    }
}
