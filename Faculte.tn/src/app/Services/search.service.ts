import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../Services/user.service'; // Import UserService to get the token

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private apiUrl = 'https://facultetnbackend.onrender.com/api'; // Update with your API URL

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

  searchUsers(query: string): Observable<any[]> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/user/search?query=${query}`;
    return this.http.get<any[]>(url, { headers });
  }

  searchPosts(query: string): Observable<any[]> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/post/search?query=${query}`;
    return this.http.get<any[]>(url, { headers });
  }
}
