import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Component({
  selector: 'app-teacherloginform',
  templateUrl: './teacherloginform.component.html',
  styleUrls: ['./teacherloginform.component.css']
})
export class TeacherloginformComponent {

  firstName: string = '';
  lastName: string = '';
  number: string = '';
  email: string = '';
  successMessage: string = '';
  errorMessage: string = '';

  constructor(private http: HttpClient) { }

  submitForm() {
    const registrationRequest = {
      firstName: this.firstName,
      lastName: this.lastName,
      number: this.number,
      email: this.email
    };
  
    this.http.post('http://localhost:8080/api/user/request/teacher', registrationRequest, { responseType: 'text' })
      .subscribe(
        response => {
          console.log('Email sent successfully:', response);
          this.successMessage = response; // Assign plain text response to success message
          this.errorMessage = ''; // Clear error message if any
        },
        error => {
          console.error('Error sending email:', error);
          this.errorMessage = 'Failed to send email. Please try again.'; // Display error message
          this.successMessage = ''; // Clear success message if any
        }
      );
  }
}  