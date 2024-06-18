import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../Services/user.service'; // Import UserService to get the token

@Injectable({
  providedIn: 'root'
})
export class PaidCourseEnrollmentService {

  private baseUrl = 'http://localhost:8080/paidcourseenrollment'; // Update this with your backend base URL

  constructor(private http: HttpClient, private userService: UserService) { }

  private getHeaders(): HttpHeaders {
    const token = this.userService.getToken();
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    } else {
      // Handle case where token is not available, e.g., throw an error or handle it as needed
      console.error('No token available');
      return new HttpHeaders();
    }
  }

  enrollUserInCourse(courseId: number, userId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.baseUrl}/${courseId}/enroll?userId=${userId}`, {}, { headers });
  }

  confirmPayment(enrollmentId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.post<string>(`${this.baseUrl}/confirm-payment/${enrollmentId}`, {}, { headers });
  }

  getUserEnrollments(userId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/user/${userId}`, { headers });
  }

  getEnrollmentsByCourseId(courseId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/course/${courseId}`, { headers });
  }

  deleteEnrollment(enrollmentId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.delete<string>(`${this.baseUrl}/${enrollmentId}`, { headers });
  }
}
