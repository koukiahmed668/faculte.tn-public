import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PaidCourseService } from '../Services/paid-course.service';
import { UserService } from '../Services/user.service';

@Component({
  selector: 'app-course-proposal-form',
  templateUrl: './course-proposal-form.component.html',
  styleUrls: ['./course-proposal-form.component.css']
})
export class CourseProposalFormComponent implements OnInit {
  courseName: string = '';
  courseDescription: string = '';
  coursePrice: number = 0; // Initialize to 0 or any default value
  loggedInUserId!: number;
  

  constructor(private translateService: TranslateService, private paidCourseService: PaidCourseService, private userService: UserService) { }

  ngOnInit(): void {
    this.fetchLoggedInUserId();
  }

  fetchLoggedInUserId(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.loggedInUserId = userId;
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  createProposedCourse(): void {
    // Replace 'teacherId' with the ID of the logged-in teacher
    const teacherId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null

    this.paidCourseService.createProposedCourse(this.courseName, this.courseDescription, this.coursePrice, teacherId)
      .subscribe(
        (response: any) => {
          console.log('Proposed course created successfully:', response);
          // Optionally, you can perform additional actions upon successful creation, such as showing a success message
        },
        (error: any) => {
          console.error('Error creating proposed course:', error);
          // Optionally, you can show an error message or perform other actions upon unsuccessful creation
        }
      );
  }
}
