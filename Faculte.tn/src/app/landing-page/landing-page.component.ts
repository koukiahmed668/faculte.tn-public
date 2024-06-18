import { Component } from '@angular/core';

@Component({
  selector: 'app-landing-page',
  templateUrl: './landing-page.component.html',
  styleUrls: ['./landing-page.component.css']
})
export class LandingPageComponent {
  firstName: string = '';
  lastName: string = '';
  number: string = '';
  email: string = '';
  password: string = '';
  educationLevel: string = '';
  year: string = '';     // to store the selected year (1st, 2nd, 3rd)
  
  showStudentForm: boolean = false;
  showTeacherForm: boolean = false;
  showQuestionModal: boolean = false;
  

  
  openQuestionModal() {
    this.showQuestionModal = true;
  }

  closeQuestionModal() {
    this.showQuestionModal = false;
  }

  activeStudentForm() {
    this.showStudentForm = true;
    this.showTeacherForm = false;
    this.showQuestionModal = false;
  }

  activeTeacherForm() {
    this.showStudentForm = false;
    this.showTeacherForm = true;
    this.showQuestionModal = false;
  }

  submitStudentForm() {
    console.log('Education Level:', this.educationLevel);
    console.log('Year:', this.year);  }
    submitTeacherForm() {
    }
}
