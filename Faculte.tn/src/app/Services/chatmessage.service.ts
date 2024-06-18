import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../Services/user.service'; // Import UserService to get the token

@Injectable({
  providedIn: 'root'
})
export class ChatmessageService {
  private apiUrl = 'https://facultetnbackend.onrender.com/api/messages'; // Adjust the API URL according to your backend endpoint

  constructor(private http: HttpClient, private userService: UserService) { }

  private getHeaders(): HttpHeaders {
    const token = this.userService.getToken();
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      });
    } else {
      // Handle case where token is not available, e.g., throw an error or handle it as needed
      console.error('No token available');
      return new HttpHeaders({
        'Content-Type': 'application/json'
      });
    }
  }

  getChatMessages(chatRoomId: number): Observable<any[]> {
    const headers = this.getHeaders();
    // Make an HTTP GET request to fetch chat messages for the specified chat room ID
    return this.http.get<any[]>(`${this.apiUrl}/chat-room/${chatRoomId}/messages`, { headers });
  }

  editMessage(messageId: number, newContent: string): Observable<any> {
    const headers = this.getHeaders();
    return this.http.put<any>(`${this.apiUrl}/edit/${messageId}`, newContent, { headers });
  }
}
