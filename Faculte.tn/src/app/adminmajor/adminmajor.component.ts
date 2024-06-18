import { Component, OnInit } from '@angular/core';
import { MajorService } from '../Services/major.service'; 
import { SubjectService } from '../Services/subject.service'; 
import {SpecialtyService} from '../Services/specialty.service';
@Component({
  selector: 'app-adminmajor',
  templateUrl: './adminmajor.component.html',
  styleUrls: ['./adminmajor.component.css']
})
export class AdminmajorComponent implements OnInit {
  majors: any[] = [];
  specialties: any[] = [];
  subjects: any[] = []; // Add this line to declare the subjects property
  showAddForm: string = ''; // Modify to string type
  newMajorName: string = '';
  newSpecialtyName: string = '';
  newSubjectName: string = '';
  selectedMajorId: number | null = null; 
  selectedSpecialtyId!:number
  enrollmentYear: string = '';
  specialtyFormVisible: boolean = false;
  selectedSpecialty: any;

  constructor(private majorService: MajorService,
              private specialtyService:SpecialtyService,
              private subjectService: SubjectService) { }

  ngOnInit(): void {
    this.getAllMajors();
  }

  getAllMajors(): void {
    this.majorService.getAllMajors()
      .subscribe(
        (majors: any[]) => {
          this.majors = majors.map(major => ({...major, editing: false, updatedName: major.name}));
          this.majors.forEach(major => {
            this.viewSpecialtiesByMajor(major.id); // Pass major.id as argument
          });
        },
        (error) => {
          console.error('Error fetching majors:', error);
        }
      );
  }

  toggleAddForm(formType: string, majorId: number | null = null): void {
    this.showAddForm = formType;
    this.selectedMajorId = majorId;
    if (!this.showAddForm) {
      this.newMajorName = '';
      this.newSubjectName = '';
      this.enrollmentYear = '';
    }
  }

  addMajor(): void {
    const newMajor = { name: this.newMajorName }; // Create a new major object using the value from newMajorName
    this.majorService.addMajor(newMajor)
        .subscribe(
            () => {
                this.getAllMajors();
                this.showAddForm = '';
            },
            (error) => {
                console.error('Error adding major:', error);
            }
        );
}

addSpecialty(newSpecialtyName: string, majorId: number): void {
  const specialtyData = { name: newSpecialtyName };
  this.specialtyService.addSpecialtyToMajor(majorId, specialtyData)
    .subscribe(
      () => {
        // Refresh the list of majors and specialties
        this.getAllMajors();
        this.showAddForm = '';
      },
      (error) => {
        console.error('Error adding specialty:', error);
      }
    );
}




  addSubject(newSubjectName: string, enrollmentYear: string): void {
    if (this.selectedMajorId !== null && this.selectedSpecialtyId !== null) {
      const subjectData = { name: newSubjectName, enrollmentYear: enrollmentYear };
      this.subjectService.addSubject(this.selectedMajorId, this.selectedSpecialtyId,enrollmentYear, subjectData).subscribe(
        () => {
          this.getAllMajors();
          this.showAddForm = '';
        },
        (error) => {
          console.error('Error adding subject:', error);
        }
      );
    }
  }
  

  toggleEdit(major: any): void {
    major.editing = !major.editing;
    if (!major.editing) {
      // Call API to update major name
    }
  }

  submitEdit(major: any): void {
    this.majorService.updateMajor(major.id, { name: major.updatedName })
        .subscribe(
            () => {
                // If update is successful, set editing to false to exit edit mode
                major.editing = false;
                // Optionally, refresh the list of majors
                this.getAllMajors();
            },
            (error) => {
                console.error('Error updating major:', error);
            }
        );
  }
  
  deleteMajor(majorId: number): void {
    this.majorService.deleteMajor(majorId)
        .subscribe(
            () => {
                // If deletion is successful, refresh the list of majors
                this.getAllMajors();
            },
            (error) => {
                console.error('Error deleting major:', error);
            }
        );
  }
  
  viewSubjectsBySpecialty(specialtyId: number): void {
    this.subjectService.getSubjectsBySpecialtyId(specialtyId)
      .subscribe(
        (subjects: any[]) => {
          this.subjects = subjects;
        },
        (error) => {
          console.error('Error fetching subjects:', error);
        }
      );
  }


  submitEditSubject(subject: any): void {
    this.subjectService.editSubject(subject.id, { name: subject.updatedName })
        .subscribe(
            () => {
                // Refresh the list of subjects after editing
                this.viewSubjectsBySpecialty(this.selectedMajorId!);
            },
            (error) => {
                console.error('Error editing subject:', error);
            }
        );
}

cancelEditSubject(subject: any): void {
    subject.editing = false;
}

deleteSubject(subjectId: number): void {
    this.subjectService.deleteSubject(subjectId)
        .subscribe(
            () => {
                // Refresh the list of subjects after deleting
                this.viewSubjectsBySpecialty(this.selectedMajorId!);
            },
            (error) => {
                console.error('Error deleting subject:', error);
            }
        );
}

toggleEditSubjectForm(subject: any): void {
  subject.editing = true;
  subject.updatedName = subject.name; // Initialize updatedName with current subject name
} 


viewSpecialtiesByMajor(majorId: number): void {
  // Call the service method to get specialties by major ID
  this.specialtyService.getAllSpecialtiesByMajor(majorId)
    .subscribe(
      (specialties: any[]) => {
        // Store the fetched specialties
        this.specialties = specialties;
      },
      (error) => {
        console.error('Error fetching specialties:', error);
      }
    );
}

deleteSpecialty(specialtyId: number): void {
  this.specialtyService.deleteSpecialty(specialtyId)
    .subscribe(
      () => {
        // If deletion is successful, refresh the list of specialties
        this.viewSpecialtiesByMajor(this.selectedMajorId!);
      },
      (error) => {
        console.error('Error deleting specialty:', error);
      }
    );
}

toggleEditSpecialtyForm(specialty: any): void {
  specialty.editing = true;
  specialty.updatedName = specialty.name; // Initialize updatedName with current specialty name
}

submitEditSpecialty(specialty: any): void {
  this.specialtyService.updateSpecialty(specialty.id, { name: specialty.updatedName })
    .subscribe(
      () => {
        // Refresh the list of specialties after editing
        this.viewSpecialtiesByMajor(this.selectedMajorId!);
      },
      (error) => {
        console.error('Error editing specialty:', error);
      }
    );
}

cancelEditSpecialty(specialty: any): void {
  specialty.editing = false;
}

}