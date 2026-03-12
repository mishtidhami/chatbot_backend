package com.upcl.chatbot.controller;

import com.upcl.chatbot.dto.ChatRequest;
import com.upcl.chatbot.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * POST /api/chat/message
     * Body: { "accountNumber": "ACC12345", "message": "mera bill kitna hai" }
     * Returns: plain string (the AI reply)
     *
     * Matches Angular: this.http.post('/api/chat/message', { accountNumber, message }, { responseType: 'text' })
     */
    @PostMapping(value = "/message", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> sendMessage(@Valid @RequestBody ChatRequest request) {
        String reply = chatService.processMessage(
                request.getAccountNumber().trim(),
                request.getMessage().trim()
        );
        return ResponseEntity.ok(reply);
    }

    /** Health check */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("UPCL Chatbot Backend is running ✅");
    }
}
