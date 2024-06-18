import { Component, OnInit } from '@angular/core';
import { PaidCourseService } from '../Services/paid-course.service';
import { UserService } from '../Services/user.service';
import { PaidCourseEnrollmentService } from '../Services/paid-course-enrollment.service';

@Component({
  selector: 'app-admincourse',
  templateUrl: './admincourse.component.html',
  styleUrls: ['./admincourse.component.css']
})
export class AdmincourseComponent implements OnInit {

  courses: any[] = [];
  proposedCourses: any[] = []; // Initialize as empty array
  selectedCourse: any;
  isAddCourseFormOpen: boolean = false;
  newCourse: any = {};
  teachers: any[] = [];
  enrollments: any[] = [];

  constructor(private courseService: PaidCourseService, private userService: UserService, private enrollmentService: PaidCourseEnrollmentService) { }

  ngOnInit(): void {
    this.loadCourses();
    this.loadTeachers();
    this.loadProposedCourses(); 

  }

  loadCourses() {
    this.courseService.getAllCourses().subscribe(
      (data) => {
        this.courses = data;
      },
      (error) => {
        console.log('Error fetching courses:', error);
      }
    );
  }
  loadProposedCourses() {
    this.courseService.getProposedCourses().subscribe(
      (data) => {
        this.proposedCourses = data;
        console.log('prop',this.proposedCourses);
      },
      (error) => {
        console.log('Error fetching proposed courses:', error);
      }
    );
  }

  loadTeachers() {
    this.userService.getTeachers().subscribe(
      (data) => {
        console.log('Teachers:', data);
        this.teachers = data;
      },
      (error) => {
        console.log('Error fetching teachers:', error);
      }
    );
  }

  openAddCourseForm() {
    this.isAddCourseFormOpen = true;
    this.newCourse = {};
  }

  onFileSelected(event: any) {
        // Retrieve the selected file
        const file: File = event.target.files[0];
        
        // Do something with the selected file, such as storing it in a property
        this.newCourse.file = file;
    }
    saveCourse() {
      // Ensure all required parameters are provided
      const { name, description, price, meetingDateTime, teacherId, file } = this.newCourse;
    
      // Check if any of the required parameters are missing
      if (!name || !description || !price || !meetingDateTime || !teacherId) {
        console.error('Missing required parameters');
        return;
      }
    
      // Call the createCourse method with all the required parameters
      this.courseService.createCourse(name, description, price, meetingDateTime, teacherId, file).subscribe(
        (data) => {
          console.log('Course created successfully:', data);
          this.loadCourses();
          this.isAddCourseFormOpen = false;
        },
        (error) => {
          console.log('Error creating course:', error);
        }
      );
    }
    
    
    

  selectCourse(course: any) {
    this.selectedCourse = course;
    this.fetchEnrollments(course.id);
  }

  updateCourse() {
    if (this.selectedCourse) {
      this.courseService.updateCourse(this.selectedCourse.id, this.selectedCourse).subscribe(
        (data) => {
          console.log('Course updated successfully:', data);
          this.selectedCourse = null;
          this.loadCourses();
        },
        (error) => {
          console.log('Error updating course:', error);
        }
      );
    }
  }

  deleteCourse(courseId: number) {
    this.courseService.deleteCourse(courseId).subscribe(
      (data) => {
        console.log('Course deleted successfully:', data);
        this.loadCourses();
      },
      (error) => {
        console.log('Error deleting course:', error);
      }
    );
  }

  fetchEnrollments(courseId: number) {
    this.enrollmentService.getEnrollmentsByCourseId(courseId).subscribe(
      (data) => {
        console.log('Enrollments:', data);
        this.enrollments = data;
      },
      (error) => {
        console.log('Error fetching enrollments:', error);
      }
    );
  }
}
