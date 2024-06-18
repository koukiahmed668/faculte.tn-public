import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../Services/user.service';


@Component({
  selector: 'app-studentloginform',
  templateUrl: './studentloginform.component.html',
  styleUrls: ['./studentloginform.component.css']
})
export class StudentloginformComponent implements OnInit {
  signupForm!: FormGroup;
  errorMessage: string = '';
  successMessage: string = ''; 

  showVerificationMessage: boolean = false;

  constructor(private userService: UserService,private http: HttpClient, private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      number: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      majorName: ['', Validators.required],
      specialtyName: ['', Validators.required],
      enrollmentYear: ['', Validators.required],

    });
  }

  save() {
    if (this.signupForm.valid) {
      let studentData = this.signupForm.value;

      this.userService.saveStudent(studentData).subscribe(
        (resultData: string) => {
          console.log(resultData);
          this.successMessage = 'Student registered successfully please check your email to activate your account';
          this.errorMessage = ''; // Clear error message
          this.signupForm.reset(); // Reset the form

          if (typeof resultData === 'string' && resultData.includes('Email verification required')) {
            this.showVerificationMessage = true;
            console.log('Verification message should be shown');

          }
        },
        (error: HttpErrorResponse) => {
          console.error(error);
          if (error.status === 409 && error.error === 'DuplicateEmail') {
            // Handle duplicate email error
            this.signupForm.get('email')?.setErrors({ duplicateEmail: true });
            this.errorMessage = 'This email is already in use. Please choose another one.';
          } else {
            // Handle other errors
            if (error.error instanceof ErrorEvent) {
              // Client-side error
              this.errorMessage = 'An unexpected error occurred on the client side. Please try again later.';
            } else {
              // Server-side error
              this.errorMessage = 'An unexpected error occurred on the server side. Please try again later.';
            }
          }
        }
      );
    } else {
      // Handle form validation errors
      this.errorMessage = "Please fill out all required fields with valid data.";
    }
  }
}