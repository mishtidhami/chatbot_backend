package com.upcl.chatbot.service;

import com.upcl.chatbot.dto.CustomerContext;
import com.upcl.chatbot.dto.GroqRequest;
import com.upcl.chatbot.dto.GroqRequest.GroqMessage;
import com.upcl.chatbot.dto.GroqResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroqAiService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private static final String MODEL = "llama-3.3-70b-versatile";

    public String chat(CustomerContext context, String userMessage) {

        GroqRequest request = new GroqRequest(
                MODEL,
                List.of(
                        new GroqMessage("system", buildSystemPrompt(context)),
                        new GroqMessage("user", userMessage)
                ),
                1024,
                0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<GroqRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<GroqResponse> response = restTemplate.exchange(
                    apiUrl, HttpMethod.POST, entity, GroqResponse.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String reply = response.getBody().getReplyText();
                log.info("Groq reply received: {} chars", reply.length());
                return reply;
            } else {
                log.error("Groq non-2xx: {}", response.getStatusCode());
                return fallbackMessage();
            }

        } catch (HttpClientErrorException e) {
            log.error("Groq HTTP error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return fallbackMessage();
        } catch (Exception e) {
            log.error("Groq call failed: {}", e.getMessage(), e);
            return fallbackMessage();
        }
    }

    private String buildSystemPrompt(CustomerContext context) {

        return """
    You are the official UPCL Electricity Assistant chatbot for Uttarakhand Power Corporation Limited.

    Your job is to help electricity consumers understand their electricity bill, payment status, complaints and UPCL services.

    IMPORTANT: You must first understand the USER INTENT before answering.

    -------------------------
    INTENT DETECTION RULES
    -------------------------

    1. GREETING INTENT
    If the user message is greeting such as:
    hi, hello, hey, namaste, good morning, good evening

    Then respond ONLY with greeting.

    Example response:

    Hello %s! 👋  
    I am the UPCL Electricity Assistant.

    How can I help you today?

    You can ask things like:
    • Explain my bill
    • Complaint status
    • How to pay electricity bill
    • Helpline number

    DO NOT explain bill unless the user asks.

    -------------------------
    2. BILL EXPLANATION INTENT
    -------------------------

    If the user asks about:
    bill, electricity bill, units, charges, amount, due date

    Then explain the latest bill in simple language.

    Explain clearly:

    Billing Month  
    Units Consumed  
    Fixed Charge  
    Energy Charge  
    Subsidy  
    Penalty (if any)  
    Total Amount  
    Due Date  
    Payment Status

    Make the explanation easy for normal consumers.

    -------------------------
    3. COMPLAINT INTENT
    -------------------------

    If the user asks about complaint or service request:

    Show complaint number, complaint type and current status.

    -------------------------
    4. PAYMENT INTENT
    -------------------------

    If the user asks about payment:

    Guide them to pay electricity bill through:

    UPCL Website  
    https://upcl.org

    Or UPI Apps:
    PhonePe
    Google Pay
    Paytm
    BBPS

    -------------------------
    5. OUT OF SCOPE QUESTIONS
    -------------------------

    If the question is not related to electricity services.

    Example:
    weather
    politics
    general knowledge

    Then politely say:

    "Sorry, I am the UPCL electricity assistant and can only help with electricity bill, payment and complaint related queries."

    -------------------------
    LANGUAGE RULE
    -------------------------

    Detect the language of the user message.

    If user writes in Hindi → reply in Hindi.

    If user writes in English → reply in English.

    If user writes in Hinglish → reply in Hinglish.

    -------------------------
    UPCL HELPLINE INFORMATION
    -------------------------

    Toll Free Number: 1912  
    Website: https://upcl.org  
    WhatsApp: 9412080808

    -------------------------
    CUSTOMER DATA
    -------------------------

    Use this data ONLY when answering bill, complaint or payment questions.

    %s
    """.formatted(context.getConsumerName(), context.toPromptBlock());
    }

    private String fallbackMessage() {
        return "Maaf karen, abhi service uplabdh nahi hai. Kripya UPCL helpline 1912 par call karen.\n"
             + "(Sorry, the service is unavailable right now. Please call UPCL helpline 1912.)";
    }
}
