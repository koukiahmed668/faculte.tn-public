import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SubjectService {

  private apiUrl = 'https://facultetnbackend.onrender.com/api/subjects';

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

  getAllSubjectsUnauthenticated(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/getalladmin`, { headers });
  }

  getSubjectsBySpecialtyId(specialtyId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/subjects/${specialtyId}`, { headers });
  }

  addSubject(majorId:number, specialtyId: number, enrollmentYear: string, subject: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.apiUrl}/${majorId}/${specialtyId}/${enrollmentYear}`, subject, { headers });
  }

  editSubject(subjectId: number, updatedSubject: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.put<any>(`${this.apiUrl}/${subjectId}`, updatedSubject, { headers });
  }

  deleteSubject(subjectId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/${subjectId}`, { headers });
  }
}
