package com.cdac.service;

import java.util.List;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordSuggestionService {

    @Autowired
    private ChatClient chatClient;

    public String getBetterKeywords(List<String> extractedKeywords, String domain) {
        String promptText = "Given the extracted keywords: " + String.join(", ", extractedKeywords) +
                " from website " + domain +
                ", suggest 5 better SEO keywords to improve Google search ranking. Return them as a comma-separated list.";

        Prompt prompt = new Prompt(new UserMessage(promptText));

        return chatClient.call(prompt)
                         .getResult()
                         .getOutput()
                         .getContent();
    }
}
