import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { UserService } from '../Services/user.service'; // Import UserService to get the token

@Injectable({
  providedIn: 'root'
})
export class SpecialtyService {
  private apiUrl = 'https://facultetnbackend.onrender.com/api/specialties';

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

  addSpecialtyToMajor(majorId: number, specialty: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.post<any>(`${this.apiUrl}/add/${majorId}`, specialty, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  deleteSpecialty(specialtyId: number): Observable<void> {
    const headers = this.getHeaders();
    return this.http.delete<void>(`${this.apiUrl}/${specialtyId}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  updateSpecialty(specialtyId: number, updatedSpecialty: any): Observable<any> {
    const headers = this.getHeaders();
    return this.http.put<any>(`${this.apiUrl}/${specialtyId}`, updatedSpecialty, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getAllSpecialtiesByMajor(majorId: number): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/major/${majorId}`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    console.error('An error occurred:', error);
    return throwError(error);
  }
}
