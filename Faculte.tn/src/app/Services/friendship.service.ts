import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  private baseUrl = 'https://facultetnbackend.onrender.com/api/friendship';

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

  getFriendshipStatus(requesterId: number, receiverId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.get<string>(`${this.baseUrl}/${requesterId}/${receiverId}`, { headers });
  }

  sendFriendRequest(requesterId: number, receiverId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.baseUrl}/friend-request/${requesterId}/${receiverId}`, null, { headers });
  }

  cancelFriendRequest(requesterId: number, receiverId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.delete<any>(`${this.baseUrl}/cancel-friend-request/${requesterId}/${receiverId}`, { headers });
  }

  acceptFriendRequest(userId: number, loggedInUserId: number): Observable<any> {
    const headers = this.getHeaders();
    const params = { loggedInUserId: loggedInUserId.toString() };
    return this.http.put<any>(`${this.baseUrl}/accept-friend-request/${userId}`, null, { headers, params });
  }

  rejectFriendRequest(userId: number, loggedInUserId: number): Observable<any> {
    const headers = this.getHeaders();
    const params = { loggedInUserId: loggedInUserId.toString() };
    return this.http.put<any>(`${this.baseUrl}/reject-friend-request/${userId}`, null, { headers, params });
  }

  deleteFriendship(userId1: number, userId2: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.delete<any>(`${this.baseUrl}/delete-friendship/${userId1}/${userId2}`, { headers });
  }

  getFriends(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/friends/${userId}`, { headers });
  }

  

  

}
