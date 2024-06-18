import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  email: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  token: string = '';

  newPassword: string = '';
  confirmPassword: string = '';

  // Add a flag to indicate whether the form should be visible
  showForm: boolean = true;

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
      // If token exists, hide the form
      if (this.token) {
        this.showForm = false;
      }
    });
  }

  submitForm() {
    if (!this.email) {
      this.errorMessage = 'Please enter your email.';
      return;
    }

    // Send email to initiate password reset
    this.http.post<any>('http://localhost:8080/api/user/forgot-password', null, { params: { email: this.email } })
      .subscribe(
        response => {
          console.log('Response:', response);
          // Show success message and clear email field
          this.successMessage = 'Check your email for instructions on resetting your password.';
          this.email = '';
          this.errorMessage = ''; // Clear any previous error messages
        },
        error => {
          console.error('Error:', error);
          // Show error message
          this.errorMessage = 'Failed to initiate password reset. Please try again later.';
          this.successMessage = ''; // Clear any previous success messages
        }
      );
  }

  resetPassword() {
    // Check if passwords match
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    // Send reset password request
    this.http.post<any>('http://localhost:8080/api/user/reset-password', null, { params: { token: this.token, newPassword: this.newPassword } })
      .subscribe(
        response => {
          console.log('Response:', response);
          // Show success message and clear fields
          this.successMessage = 'Password reset successfully.';
          this.newPassword = '';
          this.confirmPassword = '';
          this.errorMessage = ''; // Clear any previous error messages
        },
        error => {
          console.error('Error:', error);
          // Show error message
          this.errorMessage = 'Failed to reset password. Please try again later.';
          this.successMessage = ''; // Clear any previous success messages
        }
      );
  }
}

