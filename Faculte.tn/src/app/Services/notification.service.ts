import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private baseUrl = 'https://facultetnbackend.onrender.com/api/notifications';

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

  getAllNotificationsByUserId(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/${userId}`, { headers });
  }

  updateNotification(notification: any): Observable<void> {
    const headers = this.getHeaders();
    return this.http.put<void>(`${this.baseUrl}/update`, notification, { headers });
  }

  deleteNotification(notificationId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.baseUrl}/${notificationId}`, { headers });
  }
}
