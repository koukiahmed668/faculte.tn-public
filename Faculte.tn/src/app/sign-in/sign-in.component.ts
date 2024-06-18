import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../Services/user.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent {
  email: string = '';
  password: string = '';
  loginError: string = '';

  constructor(private router: Router, private http: HttpClient, private userService: UserService) {}

  login() {
    if (!this.email || !this.password) {
      this.loginError = 'Please fill out all fields.';
      return;
    }
    this.userService.login(this.email, this.password).subscribe(
      (resultData: any) => {
        if (resultData.message === 'Login Success') {
          this.userService.setToken(resultData.token); // Store the token in local storage
          this.userService.redirectBasedOnRole(resultData.role);
        } else {
          this.loginError = 'Incorrect email or password.';
        }
      },
      (error) => {
        console.error('Login failed:', error);
        this.loginError = 'An error occurred during login. Please try again.';
      }
    );
  }
}
