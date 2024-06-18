import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog'; // Import MatDialogRef
import { PaidCourseEnrollmentService } from '../Services/paid-course-enrollment.service';
import { PaidCourseService } from '../Services/paid-course.service';
import { UserService } from '../Services/user.service'; // Import UserService



@Component({
  selector: 'app-course-details-popup',
  templateUrl: './course-details-popup.component.html',
  styleUrls: ['./course-details-popup.component.css']
})
export class CourseDetailsPopupComponent implements OnInit {
  course: any; // Variable to store course details
  courseId: number; // Course ID passed from the parent component
  teacherFirstName!: string; // Variable to store teacher's first name

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<CourseDetailsPopupComponent>, // Inject MatDialogRef
    private paidCourseEnrollmentService: PaidCourseEnrollmentService,
    private paidCourseService: PaidCourseService, // Inject the PaidCourseService
    private userService: UserService
  ) {
    this.courseId = data.courseId; // Assign course ID passed from parent component
  }

  ngOnInit(): void {
    // Call the method to fetch the course by its ID when the component initializes
    this.loadCourseById();
  }

  loadCourseById(): void {
    this.paidCourseService.getCourseById(this.courseId).subscribe(
      (course: any) => {
        this.course = course;
        // Fetch the teacher's first name using the teacher_id
        this.userService.getUserById().subscribe(
          (user: any) => {
            this.teacherFirstName = user.firstName; // Assign teacher's first name
          },
          (error) => {
            console.error('Error fetching teacher details:', error);
          }
        );
      },
      (error) => {
        console.error('Error fetching course by ID:', error);
      }
    );
  }

  enroll(): void {
    const userId = this.data.loggedInUserId;
    const courseId = this.courseId;

    // Call the service method to enroll the user in the course
    this.paidCourseEnrollmentService.enrollUserInCourse(courseId, userId).subscribe(
      () => {
        console.log('User enrolled successfully.');
        // Optionally, perform any actions after enrollment
      },
      error => {
        console.error('Error enrolling user:', error);
        // Optionally, handle errors related to enrollment
      }
    );
  }

  closePopup(): void {
    this.dialogRef.close(); // Close the dialog when the close button is clicked
  }
}
