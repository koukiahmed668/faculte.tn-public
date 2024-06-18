import { Component, OnInit } from '@angular/core';
import { UserService } from '../Services/user.service'; // Update the path
import { MajorService } from '../Services/major.service'; // Update the path

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  students: any[] = []; // Array to store student details
  selectedStudent: any = null; // Variable to store the selected student for editing
  teachers: any[] = []; // Assuming the type of teachers is any array
  isStudentsSelected: boolean = true; // Flag to indicate if students are selected initially
  selectedList: any[]; // Reference to the currently selected list
  isAddTeacherFormOpen: boolean = false;
  newTeacher: any = {}; // Define the newTeacher property
  majors: any[] = []; // Array to store major details
  isMajorSelected: boolean = false; // Add this variable to track the visibility of the majors/subjects table


  constructor(private userService: UserService,private majorService:MajorService) {
    this.selectedList = this.students;

  }
  
  
  ngOnInit(): void {
    this.fetchStudents();
  }

  fetchStudents(): void {
    this.userService.getStudents().subscribe(
      (students: any[]) => {
        this.students = students;
        console.log('students',students);
      },
      (error) => {
        console.error('Error fetching students:', error);
      }
    );
  }

  

  deleteStudent(studentId: number): void {
    if (confirm('Are you sure you want to delete this student?')) {
      this.userService.deleteStudent(studentId).subscribe(
        () => {
          // Remove the deleted student from the array
          this.students = this.students.filter(student => student.id !== studentId);
        },
        (error) => {
          console.error('Error deleting student:', error);
        }
      );
    }
  }

  selectStudent(student: any): void {
    this.selectedStudent = student;
  }

  updateStudent(): void {
    this.userService.updateStudent(this.selectedStudent.id, this.selectedStudent).subscribe(
      (response) => {
        console.log('Student updated successfully:', response);
        // Reset the selectedStudent variable after successful update
        this.selectedStudent = null;
      },
      (error) => {
        console.error('Error updating student:', error);
      }
    );
    this.selectedStudent = null;

  }











}
