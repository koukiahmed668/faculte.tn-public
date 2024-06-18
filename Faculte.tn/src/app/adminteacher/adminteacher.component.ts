import { Component } from '@angular/core';
import { UserService } from '../Services/user.service'; // Update the path

@Component({
  selector: 'app-adminteacher',
  templateUrl: './adminteacher.component.html',
  styleUrl: './adminteacher.component.css'
})
export class AdminteacherComponent {

  students: any[] = []; // Array to store student details
  selectedStudent: any = null; // Variable to store the selected student for editing
  teachers: any[] = []; // Assuming the type of teachers is any array
  isStudentsSelected: boolean = true; // Flag to indicate if students are selected initially
  selectedList: any[]; // Reference to the currently selected list
  isAddTeacherFormOpen: boolean = false;
  newTeacher: any = {}; // Define the newTeacher property
  majors: any[] = []; // Array to store major details
  isMajorSelected: boolean = false; // Add this variable to track the visibility of the majors/subjects table


  constructor(private userService: UserService) {
    this.selectedList = this.students;

  }




  openAddTeacherForm() {
    this.isAddTeacherFormOpen = true;
  }


  saveTeacher() {
    // Call the saveTeacher method of UserService
    this.userService.saveTeacher(this.newTeacher).subscribe(
      (response) => {
        console.log('Teacher saved successfully:', response);
        // Reset the newTeacher object
        this.newTeacher = {};
        // Close the form
        this.isAddTeacherFormOpen = false;
        // Refresh the page
        window.location.reload();
      },
      (error) => {
        console.error('Error saving teacher:', error);
      }
    );
  }
  

  
  ngOnInit(): void {
    this.fetchTeachers(); 
  }



  fetchTeachers(): void {
    this.userService.getTeachers().subscribe(
      (teachers: any[]) => {
        this.teachers = teachers;
        this.selectedList = this.isStudentsSelected ? this.students : this.teachers;
      },
      (error) => {
        console.error('Error fetching teachers:', error);
      }
    );
  }

  deleteStudent(studentId: number): void {
    if (confirm('Are you sure you want to delete this teacher?')) {
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
