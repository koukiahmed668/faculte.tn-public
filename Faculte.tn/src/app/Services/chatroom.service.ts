import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class ChatroomService {

  private baseUrl = 'https://facultetnbackend.onrender.com/api/chatrooms';

  constructor(private http: HttpClient, private userService: UserService) { }

  private getHeaders(): HttpHeaders {
    const token = this.userService.getToken();
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      console.error('No token available');
      return new HttpHeaders();
    }
  }

  getChatRoomsByUserId(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`, { headers });
  }

  getChatRoomById(roomId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${this.baseUrl}/${roomId}`, { headers });
  }

  getChatRoomByUserIds(loggedInUserId: number, friendId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${this.baseUrl}/user/${loggedInUserId}/friend/${friendId}`, { headers });
  }
  
}
