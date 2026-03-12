package com.upcl.chatbot.service;

import com.upcl.chatbot.dto.CustomerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final CustomerService customerService;
    private final GroqAiService groqAiService;

    public String processMessage(String accountNumber, String message) {
        log.info("Chat request - account: {}, message: {}", accountNumber, message);

        Optional<CustomerContext> contextOpt = customerService.buildContext(accountNumber);

        if (contextOpt.isEmpty()) {
            return "Account number nahi mili: " + accountNumber + "\n"
                 + "Kripya sahi Account Number daalen ya UPCL helpline 1912 par call karen.\n"
                 + "(Account number not found. Please enter a valid account number or call UPCL helpline 1912.)";
        }

        return groqAiService.chat(contextOpt.get(), message);
    }
}
