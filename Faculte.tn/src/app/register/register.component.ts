import { Component } from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls:[ './register.component.css']
})
export class RegisterComponent {
  firstName: string = '';
  lastName: string = '';
  number: string = '';
  email: string = '';
  password: string = '';
  educationLevel: string = '';
  year: string = '';     // to store the selected year (1st, 2nd, 3rd)
  currentStep: string = ''; 
  
  
 
}

