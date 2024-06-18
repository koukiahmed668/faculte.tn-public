import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = 'https://facultetnbackend.onrender.com/api/reports';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('accessToken');
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      // Handle the case where token is not available
      return new HttpHeaders();
    }
  }

  reportPost(postId: number, reason: string): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.apiUrl}/reportPost?postId=${postId}&reason=${reason}`, null, { headers });
  }

  reportComment(commentId: number, reason: string): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.apiUrl}/reportComment?commentId=${commentId}&reason=${reason}`, null, { headers });
  }

  getAllReports(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}`, { headers });
  }

  getAllPendingReports(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/pending`, { headers });
  }

  deleteReport(id: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers });
  }

  deleteReportedPost(id: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/deleteReportedPost/${id}`, { headers });
  }

  deleteReportedComment(id: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/deleteReportedComment/${id}`, { headers });
  }
  getReportedPosts(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/reportedPosts`, { headers });
  }

  getReportedComments(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/reportedComments`, { headers });
  }
}
