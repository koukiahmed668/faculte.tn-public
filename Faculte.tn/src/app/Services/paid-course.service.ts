import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserService } from '../Services/user.service'; // Import UserService to get the token

@Injectable({
  providedIn: 'root'
})
export class PaidCourseService {

  private baseUrl = 'http://localhost:8080/paidcourse';

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

  createCourse(name: string, description: string, price: number, meetingDateTime: string, teacherId: number, file: File | null): Observable<any> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('price', price.toString());
    formData.append('meetingDateTime', meetingDateTime);
    formData.append('teacherId', teacherId.toString());
    if (file) {
      formData.append('file', file);
    }

    const headers = this.getHeaders();
    return this.http.post<any>(`${this.baseUrl}/create`, formData, { headers });
  }
  


  getAllCourses(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/getallpaidcourses`, { headers });
  }
  getFileData(fileId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get(`http://localhost:8080/api/files/files/${fileId}/blob`, { headers, responseType: 'blob' });
}
  getCourseById(courseId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${this.baseUrl}/${courseId}`, { headers });
  }

  updateCourse(courseId: number, updatedCourse: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.put<any>(`${this.baseUrl}/${courseId}`, updatedCourse, { headers });
  }

  deleteCourse(courseId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.delete(`${this.baseUrl}/${courseId}`, { headers, responseType: 'text' });
  }

  createProposedCourse(name: string, description: string, price: number, teacherId: number): Observable<any> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('price', price.toString());
    formData.append('teacherId', teacherId.toString());

    const headers = this.getHeaders();
    return this.http.post<any>(`${this.baseUrl}/create-proposed`, formData, { headers });
  }

  // New method to get proposed courses
  getProposedCourses(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.baseUrl}/get-proposed`, { headers });
  }
  
}
