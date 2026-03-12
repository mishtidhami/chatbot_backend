# UPCL Bill Assistant — Spring Boot Backend

## Tech Stack
- **Java 17** + Spring Boot 3.2
- **MySQL** — database `WSS_CHATBOT`
- **Groq API** (OpenAI-compatible, llama3-70b) — AI responses
- **Angular 17** frontend (separate project)

---

## Project Structure

```
src/main/java/com/upcl/chatbot/
├── ChatBotApplication.java          ← Main class
├── config/
│   ├── AppConfig.java               ← RestTemplate bean
│   └── CorsConfig.java              ← Allow Angular (localhost:4200)
├── controller/
│   ├── ChatController.java          ← POST /api/chat/message
│   └── GlobalExceptionHandler.java  ← Error handling
├── service/
│   ├── ChatService.java             ← Orchestrator
│   ├── CustomerService.java         ← MySQL data aggregation
│   └── GroqAiService.java           ← Groq API caller
├── model/
│   ├── Customer.java                ← customers table
│   ├── Bill.java                    ← bills table
│   └── Complaint.java               ← complaints table
├── repository/
│   ├── CustomerRepository.java
│   ├── BillRepository.java
│   └── ComplaintRepository.java
└── dto/
    ├── ChatRequest.java             ← Incoming request body
    ├── CustomerContext.java         ← DB → AI prompt bridge
    ├── GroqRequest.java             ← Groq API request
    └── GroqResponse.java            ← Groq API response
```

---

## Setup Steps

### 1. MySQL — Create DB & load sample data
```sql
-- Run this file in MySQL Workbench or CLI:
source src/main/resources/schema-and-data.sql
```

### 2. application.properties — Already configured
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/WSS_CHATBOT
spring.datasource.username=root
spring.datasource.password=
gemini.api.key=
gemini.api.url=https://api.groq.com/openai/v1/chat/completions
```

### 3. Build & Run
```bash
mvn clean install
mvn spring-boot:run
```
Backend starts on **http://localhost:8080**

---

## API Endpoint

### POST `/api/chat/message`
**Request body:**
```json
{
  "accountNumber": "ACC12345",
  "message": "mera bill kitna hai"
}
```
**Response:** plain text (the AI reply in Hindi or English)

### GET `/api/chat/health`
Returns `UPCL Chatbot Backend is running ✅`

---

## Angular — Update chat.service.ts
Copy `src/main/resources/chat.service.ts` into your Angular project at:
```
src/app/services/chat.service.ts
```
Make sure `HttpClientModule` is imported in your `app.module.ts` or `app.config.ts`.

---

## Sample Accounts for Testing
| Account   | Name                   | Type       |
|-----------|------------------------|------------|
| ACC12345  | Ramesh Kumar Sharma    | Domestic   |
| ACC67890  | Priya Singh            | Domestic   |
| ACC11111  | Mohan Lal Enterprises  | Commercial |
| ACC22222  | Sunita Devi            | Domestic   |

---

## Flow Diagram
```
Angular Frontend
     │  POST /api/chat/message { accountNumber, message }
     ▼
ChatController
     │
     ▼
ChatService
     ├─► CustomerService → MySQL (customers + bills + complaints)
     │         └─► builds CustomerContext (toPromptBlock)
     └─► GroqAiService
               ├─► system prompt = UPCL persona + customer data
               ├─► user message
               └─► Groq API (llama3-70b) → plain text reply
     │
     ▼
Angular (displays reply in chat bubble)
```
