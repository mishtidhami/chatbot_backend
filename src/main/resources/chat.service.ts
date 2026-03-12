import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  // Spring Boot backend
  private apiUrl = 'http://localhost:8080/api/chat/message';

  constructor(private http: HttpClient) {}

  /**
   * POST /api/chat/message
   * Body: { accountNumber, message }
   * Returns: plain text string (the AI reply)
   */
  sendMessage(accountNumber: string, message: string): Observable<string> {
    const body = { accountNumber, message };
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post(this.apiUrl, body, {
      headers,
      responseType: 'text'   // ← important: backend returns plain text
    });
  }
}
