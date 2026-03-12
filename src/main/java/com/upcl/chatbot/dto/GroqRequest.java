package com.upcl.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** DTO for the OpenAI-compatible /v1/chat/completions request (used by Groq). */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroqRequest {

    private String model;
    private List<GroqMessage> messages;

    @JsonProperty("max_tokens")
    private int maxTokens;

    private double temperature;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroqMessage {
        private String role;    // "system" | "user" | "assistant"
        private String content;
    }
}
