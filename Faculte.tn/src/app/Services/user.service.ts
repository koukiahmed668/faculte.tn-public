// user.service.ts

import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders  } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt'; // Import JwtHelperService





@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'https://facultetnbackend.onrender.com/api/user';
  private jwtToken: string | null = null;
  
  constructor(
    private http: HttpClient,
    private router: Router,
    private jwtHelper: JwtHelperService // Inject JwtHelperService
    ) {}

  setToken(token: string): void {
    localStorage.setItem('accessToken', token);
    this.jwtToken = token;
  }

  getToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getStudents(): Observable<any[]> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.getToken() 
    });
    return this.http.get<any[]>(`${this.apiUrl}/getstudents`, { headers });
  }

  getTeachers(): Observable<any[]> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.getToken() 
    });
    return this.http.get<any[]>(`${this.apiUrl}/getteachers`, { headers });
  }

  deleteStudent(studentId: number): Observable<void> {
    const url = `${this.apiUrl}/${studentId}`;
    return this.http.delete<void>(url).pipe(
      catchError(error => {
        console.error('Error deleting student:', error);
        return throwError('Error deleting student');
      })
    );
  }

  updateStudent(studentId: number, updatedStudent: any): Observable<any> {
    const url = `${this.apiUrl}/${studentId}`;
    return this.http.put<any>(url, updatedStudent).pipe(
      catchError(error => {
        console.error('Error updating student:', error);
        return throwError('Error updating student');
      })
    );
  }
  


  saveStudent(studentData: any): Observable<string> {
    const url = `${this.apiUrl}/save/student`;
    return this.http.post<string>(url, studentData);
  }

  saveTeacher(teacherData: any): Observable<string> {
    const url = `${this.apiUrl}/save/teacher`;
    return this.http.post<string>(url, teacherData);
  }



  login(email: string, password: string): Observable<any> {
    const url = `${this.apiUrl}/login`;
    const body = { email, password };
    return this.http.post<any>(url, body).pipe(
      catchError(error => {
        let errorMessage = 'An unknown error occurred';
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }
        return throwError(errorMessage);
      })
    );
  }

  redirectBasedOnRole(role: string): void {
    switch (role) {
      case 'ADMIN':
        this.router.navigateByUrl('/admin');
        break;
      case 'STUDENT':
        this.router.navigateByUrl('/timeline');
        break;
      case 'TEACHER':
        this.router.navigateByUrl('/teachertimeline');
        break;
      default:
        console.error('Unknown role:', role);
        // You can handle other roles or show an error message as needed
        break;
    }
  }

  
  isAuthenticated(): boolean {
    const token = this.getToken();
    return !!token && !this.jwtHelper.isTokenExpired(token);
  }


  isAdmin(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    const decodedToken = this.jwtHelper.decodeToken(token);
    const userRole = decodedToken.roles; // Assuming role information is stored in the JWT token
    return userRole === 'ADMIN';
  }

  isTeacher(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    const decodedToken = this.jwtHelper.decodeToken(token);
    const userRole = decodedToken.roles; // Assuming role information is stored in the JWT token
    return userRole === 'TEACHER';
  }
  
  forgotPassword(email: string): Observable<any> {
    const url = `${this.apiUrl}/forgot-password`;
    return this.http.post<any>(url, { email });
  }



  getUserId(): Observable<number> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.getToken() 
    });
    return this.http.get<number>(`${this.apiUrl}/user-id`, { headers }).pipe(
      catchError(error => {
        return throwError(error);
      })
    );
  }
  getUserById(): Observable<any> {
    const accessToken = localStorage.getItem('accessToken');

    // Check if accessToken is available
    if (!accessToken) {
      console.error('getUserById: Access token not found');
      return throwError('Access token not found');
    }

    // Decode the token to access the payload
    const decodedToken = this.jwtHelper.decodeToken(accessToken);

    // Retrieve the user ID from the decoded token
    const userId = decodedToken.userId;

    // Check if userId is available
    if (!userId) {
      console.error('getUserById: User ID not found in token');
      return throwError('User ID not found in token');
    }

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + accessToken
    });

    return this.http.get<any>(`https://facultetnbackend.onrender.com/api/user/${userId}`, { headers })
      .pipe(
        catchError(error => {
          console.error('getUserById: Error fetching user by ID', error);
          return throwError('Error fetching user by ID');
        })
      );
  }

  getUserDetails(userId: number): Observable<any> {
    return this.http.get<any>(`https://facultetnbackend.onrender.com/api/user/${userId}`); // Replace with your API endpoint
  }

  getUserSubjects(): Observable<any[]> {
    const token = localStorage.getItem('accessToken');
    if (!token) {
      console.error('No token found');
      return throwError('No token found');
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any[]>('https://facultetnbackend.onrender.com/api/subjects/getall', { headers }).pipe(
      catchError(error => {
        return throwError(error);
      })
    );
  }
  
}