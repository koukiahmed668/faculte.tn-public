import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserService } from '../Services/user.service';
import { PostService } from '../Services/post.service';
import { ThemeService } from '../Services/theme.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ShareModalComponent } from '../share-modal/share-modal.component'; // Import your share modal component
import { MatSnackBar } from '@angular/material/snack-bar'; // Import MatSnackBar
import { PaidCourseService } from '../Services/paid-course.service';
import { CourseDetailsPopupComponent } from '../course-details-popup/course-details-popup.component'
import { trigger, transition, animate, style } from '@angular/animations';
import { FriendshipService } from '../Services/friendship.service';
import { ReportService } from '../Services/report.service'; // Import the ReportService
import { MajorService } from '../Services/major.service'; // Import the MajorService
import { SpecialtyService } from '../Services/specialty.service'; // Import the SpecialtyService
import {SubjectService} from '../Services/subject.service'


@Component({
  selector: 'app-teacher-timeline',
  templateUrl: './teacher-timeline.component.html',
  styleUrls: ['./teacher-timeline.component.css'],
  animations: [
    trigger('carouselAnimation', [
      transition('void => *', [
        style({ transform: 'translateX(-100%)' }),
        animate('0.5s ease-in-out')
      ]),
      transition('* => void', [
        animate('0.5s ease-in-out', style({ transform: 'translateX(100%)' }))
      ]),
    ]),
  ],
})
export class TeacherTimelineComponent implements OnInit {

  toggleDarkMode(): void {
    this.themeService.toggleDarkMode();
  }

  loggedInUserId: number | null = null;
  subjects: any[] = [];
  majors: any[] = [];
  specialties: any[] = [];
  selectedMajorId: number | null = null;
  selectedSpecialtyId: number | null = null;
  courses: any[] = [];
  selectedSubjectId: number | null = null;
  postContent: string = '';
  selectedFiles: File[] = [];
  userId!: number;
  posts: any[] = []; // Array to store posts
  newContent: string = ''; // Declare newContent property
  newFiles: File[] = []; // Declare newFiles property
  isMyProfile: boolean = false;
  selectedCourse: any; // Define selectedCourse property
  currentIndex = 0;
  interval: any;
  showLanguageSwitcherPopup: boolean = false;
  showChatContainer: boolean = false; // Define the showChatContainer property
  friends: any[] = []; // Array to store friends
  showNotification: boolean = false;
  showChatList: boolean = false;
  showMajorList: boolean = false;
  showSpecialtyList: boolean = false;
  showSubjectList: boolean = false;  
  showCourseProposalForm: boolean = false;
  constructor(private subjectService:SubjectService,private majorService: MajorService, private specialtyService: SpecialtyService, private reportService: ReportService, private friendshipService:FriendshipService ,private paidCourseService: PaidCourseService,private snackBar: MatSnackBar, private dialog: MatDialog, private router: Router, private sanitizer: DomSanitizer, private http: HttpClient, private userService: UserService, private postService: PostService, private themeService: ThemeService) { }

  

  ngOnInit() {
    this.fetchMajors(); 
    this.startCarousel();
    this.fetchSubjects();
    this.fetchLoggedInUserId();
    this.fetchPosts(); // Fetch posts on component initialization
    this.fetchSelectedCourse(); // Replace 'courseId' with the ID of the selected course
    this.posts.forEach(post => post.showComments = false);
    this.fetchSpecialties();


    // Check if the profile belongs to the logged-in user
    this.userService.getUserId().subscribe(
      userId => {
        this.isMyProfile = userId === this.loggedInUserId;
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  startCarousel(): void {
    this.interval = setInterval(() => {
      this.moveToNextSlide();
    }, 5000); // Change slide every 5 seconds
  }

  moveToNextSlide(): void {
    this.currentIndex = (this.currentIndex + 1) % this.courses.length;
  }

  prevSlide(): void {
    this.currentIndex = (this.currentIndex - 1 + this.courses.length) % this.courses.length;
  }
  nextSlide(): void {
    this.moveToNextSlide();
  }
  ngOnDestroy(): void {
    // Clear the interval when the component is destroyed
    clearInterval(this.interval);
  }

  search(query: string): void {
    if (query.trim() !== '') {
        this.router.navigate(['/search-results'], { queryParams: { query: query } });
    }
  }

  fetchLoggedInUserId(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.loggedInUserId = userId;
        this.fetchPosts(); // Call fetchPosts after fetching the user ID
        this.posts.forEach(post => post.showComments = false);
        this.fetchFriends(); // Call fetchFriends after fetching the user ID
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  fetchMajors(): void {
    this.majorService.getAllMajors().subscribe(
      majors => {
        this.majors = majors;
        console.log(majors);
      },
      error => {
        console.error('Error fetching majors:', error);
      }
    );
  }
  
 
  
 
  

  fetchSpecialties(): void {
    if (this.selectedMajorId !== null) {
      this.specialtyService.getAllSpecialtiesByMajor(this.selectedMajorId).subscribe(
        specialties => {
          this.specialties = specialties;
          console.log(specialties);
        },
        error => {
          console.error('Error fetching specialties:', error);
        }
      );
    }
  }

  toggleMajorList(): void {
    this.showMajorList = !this.showMajorList;
  }
  
  toggleSpecialtyList(): void {
    this.showSpecialtyList = !this.showSpecialtyList;
  }

  toggleSubjectList(): void {
    this.showSubjectList = !this.showSubjectList;
  }
  

  

  onSelectMajor(majorId: number): void {
    console.log('Selected major ID:', majorId); // Check if the major ID is correct
    this.selectedMajorId = majorId;
    this.selectedSpecialtyId = null; // Reset selected specialty
    this.selectedSubjectId = null; // Reset selected subject
    this.fetchSpecialties(); // Fetch specialties for the selected major
    this.showSpecialtyList = true; // Ensure that the specialty list is shown
    this.showSubjectList = false;
  }
  
  
  onSelectSpecialty(specialtyId: number): void {
    this.selectedSpecialtyId = specialtyId;
    this.selectedSubjectId = null; // Reset selected subject
    this.fetchSubjects(); // Fetch subjects for the selected specialty
    this.showSubjectList = true;

  }


  fetchSubjects(): void {
    if (this.selectedSpecialtyId !== null) {
      this.subjectService.getSubjectsBySpecialtyId(this.selectedSpecialtyId).subscribe(
        subjects => {
          this.subjects = subjects;
        },
        error => {
          console.error('Error fetching subjects:', error);
        }
      );
    }
  }

  fetchPosts(): void {
    if (this.selectedSubjectId !== null) {
      this.postService.getPostsBySubjectId(this.selectedSubjectId).subscribe(
        posts => {
          this.posts = posts;
          this.processPosts(); // Process posts after fetching
          this.posts.forEach(post => post.showComments = false);

        },
        error => {
          console.error('Error fetching posts:', error);
        }
      );
    } else {
      // Fetch all posts if no subject is selected
      this.postService.getAllPosts().subscribe(
        posts => {
          this.posts = posts;
          this.processPosts(); // Process posts after fetching
          this.posts.forEach(post => post.showComments = false);

        },
        error => {
          console.error('Error fetching posts:', error);
        }
      );
    }
  }

  processPosts(): void {
    // Sort posts based on createdAt property (newest first)
    this.posts.sort((a, b) => {
      return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
    });
  
    // Fetch additional user information and file data for each post
    this.posts.forEach(post => {
      // Fetch user information
      this.userService.getUserById().subscribe(
        user => {
          post.authorFirstName = user.firstName; // Assuming user object has firstName and lastName properties
          post.authorLastName = user.lastName;
        },
        error => {
          console.error('Error fetching user information:', error);
        }
      );
  
    // Fetch file data for the post (if exists)
    if (post.files && post.files.length > 0) {
      post.files.forEach((file: any) => {
        this.postService.getFileData(file.id).subscribe(
          (fileData: Blob) => { // Use Blob type for binary data
              const reader = new FileReader();
              reader.onload = () => {
                  // Assuming fileData contains the binary data of the file
                  file.fileUrl = reader.result as string; // Convert binary data to data URL
              };
              reader.readAsDataURL(fileData); // Read binary data as data URL
          },
          (error: any) => {
              console.error('Error fetching file data:', error);
          }
      );
      
      });
      
    }
  });
}

 

  onSelectSubject(subjectId: number): void {
    this.selectedSubjectId = subjectId;
    this.fetchPosts(); // Fetch posts for the selected subject
  }

  onFileSelected(event: any): void {
    this.selectedFiles = event.target.files;
  }
  
  createPost(): void {
    this.postService.createPost(this.postContent, this.selectedFiles, this.selectedSubjectId)
      .then(() => {
        this.fetchPosts();
        this.postContent = '';
        this.selectedFiles = [];
        this.openToast('Post created successfully', true); // Show success toast

      })
      .catch(error => {
        console.error('Error creating post:', error);
        this.openToast('Failed to create post', false); // Show error toast

      });
  }

  toggleMenu(postId: number): void {
    // Loop through the posts and toggle the menu for the clicked post
    this.posts.forEach(post => {
      if (post.id === postId) {
        post.showMenu = !post.showMenu;
      } else {
        post.showMenu = false; // Hide menu for other posts
      }
    });
  }

  savePost(postId: number): void {
    const userId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null
    this.postService.savePost(userId, postId).subscribe(
      response => {
        console.log('Bookmark added successfully:', response);
      },
      error => {
        console.error('Error adding bookmark:', error);
      }
    );
  }

  getBookmarkedPosts(): void {
    // Fetch bookmarked posts for the logged-in user
    this.userService.getUserId().subscribe(
      userId => {
        this.postService.getBookmarkedPosts(userId).subscribe(
          bookmarkedPosts => {
            // Update the posts array with bookmarked posts
            this.posts = bookmarkedPosts;
            this.processPosts();
            console.log('bookmark', bookmarkedPosts);
          },
          error => {
            console.error('Error fetching bookmarked posts:', error);
          }
        );
      },
      error => {
        console.error('Error fetching user ID:', error);
      }
    );
  }
  
  
  

  deletePost(postId: number): void {
    const userId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null
    this.postService.deletePost(postId, userId).subscribe(
      () => {
        console.log('Post deleted successfully');
        // Call fetchPosts again to update the posts list
        this.fetchPosts();
        this.posts.forEach(post => post.showComments = false);
      },
      error => {
        console.error('Failed to delete post:', error);
        this.fetchPosts();
      }
    );
  }
  

  updatePost(postId: number, newContent: string, newFiles: File[]): void {
    const userId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null
    this.postService.updatePost(postId, userId, newContent, newFiles)
      .subscribe(
        (response: any) => {
          console.log('Post updated successfully:', response);
          // Optionally, you can update the posts array or fetch updated posts here
          // Reset newContent and newFiles after updating the post
          this.newContent = '';
          this.newFiles = [];
          // Hide the edit form
          // Assuming you have a property called showEditForm in your post object
          const updatedPost = this.posts.find(p => p.id === postId);
          if (updatedPost) {
            updatedPost.showEditForm = false;
          }
          this.fetchPosts();
          this.posts.forEach(post => post.showComments = false);

        },
        (error: any) => {
          console.error('Error updating post:', error);
        }
      );
  }

  onNewFilesSelected(event: any): void {
    this.newFiles = event.target.files;
  }

  toggleEdit(post: any): void {
    // Initialize the showEditForm property if it's undefined
    if (post.showEditForm === undefined) {
      post.showEditForm = false;
    }
    // Toggle the edit mode for the clicked post
    post.showEditForm = !post.showEditForm;
  }

  isImageFile(file: any): boolean {
    return file.fileType.startsWith('image');
  }

  isPdfFile(file: any): boolean {
    return file.fileType.startsWith('application/pdf');
  }

  sanitizeUrl(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  

  likePost(postId: number,): void {
    const userId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null
    this.postService.likePost(postId, userId).subscribe(
      (updatedLikeCount: number) => {
        // Update the like count for the specific post in your UI
        const postIndex = this.posts.findIndex(post => post.id === postId);
        if (postIndex !== -1) {
          this.posts[postIndex].likeCount = updatedLikeCount;
        }
      },
      (error: any) => {
        console.error('Error liking post:', error);
      }
    );
  }


  toggleLike(post: any) {
    post.liked = !post.liked; // Toggle the liked property
    // Optionally, send a request to your backend API to handle the like functionality
}
  
toggleComments(post: any) {
  post.showComments = !post.showComments; // Toggle the value of showComments
}

openShareModal(post: any): void {
  const dialogRef = this.dialog.open(ShareModalComponent, {
    width: '400px',
    data: { postId: post.id } // Pass post id to the modal
  });

  dialogRef.afterClosed().subscribe((additionalContent: string) => {
    if (additionalContent) {
      this.postService.sharePost(post.id, post.author.id, additionalContent).subscribe(
        response => {
          console.log('Post shared successfully:', response);
          this.openToast('Post shared successfully', true); // Show success toast
        },
        error => {
          console.error('Error sharing post:', error);
          this.openToast('Failed to share post', false); // Show error toast
        }
      );
    }
  });
}


openToast(message: string, success: boolean = false, duration: number = 2000): void {
  this.snackBar.open(message, '', {
    duration: duration,
    panelClass: [success ? 'toast-success' : 'toast-error'], // Add CSS class based on success or error
    horizontalPosition: 'center',
    verticalPosition: 'bottom'
  });
}

fetchSelectedCourse(): void {
  this.paidCourseService.getAllCourses().subscribe(
    (data) => {
      this.courses = data;
      this.courses = this.courses.filter(course => course.status === 'PROPOSED');

      this.processFormations();
    },
    (error) => {
      console.log('Error fetching courses:', error);
    }
  );
}

processFormations(): void {
  this.courses.forEach(course => {
    // Fetch file data for each formation
    if (course.file) {
      console.log('Formation with file:', course);
      this.paidCourseService.getFileData(course.file.id).subscribe(
        (fileData: Blob) => {
          const reader = new FileReader();
          reader.onload = () => {
            course.file.fileUrl = reader.result as string; // Convert binary data to data URL
          };
          reader.readAsDataURL(fileData); // Read binary data as data URL
        },
        (error: any) => {
          console.error('Error fetching file data:', error);
        }
      );
    } else {
      console.log('Formation without file:', course);
    }
  });
}

openCourseDetailsPopup(courseId: number): void {
  // Configure the dialog
  const dialogConfig = new MatDialogConfig();
  dialogConfig.data = {
    courseId: courseId,
    loggedInUserId: this.loggedInUserId // Pass the logged-in user ID if needed
  };
  dialogConfig.width = '400px'; // Set the width of the dialog as per your requirement

  // Open the dialog
  const dialogRef = this.dialog.open(CourseDetailsPopupComponent, dialogConfig);

  // Subscribe to the afterClosed event to handle actions after the dialog is closed
  dialogRef.afterClosed().subscribe(result => {
    // Perform any actions after the dialog is closed, if needed
  });
}

toggleLanguageSwitcherPopup(): void {
  this.showLanguageSwitcherPopup = !this.showLanguageSwitcherPopup;
}



fetchFriends(): void {
  if (this.loggedInUserId !== null) {
    this.friendshipService.getFriends(this.loggedInUserId).subscribe(
      (friends: any[]) => {
        this.friends = friends;
        console.log('Friends:', this.friends);
      },
      error => {
        console.error('Error fetching friends:', error);
      }
    );
  } else {
    console.error('Logged-in user ID is null');
  }
}

toggleChatContainer() {
  this.showChatContainer = !this.showChatContainer;
}


toggleNotification(): void {
  this.showNotification = !this.showNotification;
}

toggleChatList(): void {
  this.showChatList = !this.showChatList; // Toggle showChatList value
}

toggleCourseProposalForm() {
  this.showCourseProposalForm = !this.showCourseProposalForm;
}



reportPost(postId: number): void {
  const reason = prompt('Please provide a reason for reporting this post:');
  if (reason) {
    // Call the reportPost method of ReportService
    this.reportService.reportPost(postId, reason).subscribe(
      response => {
        console.log('Post reported successfully:', response);
        // Optionally, you can show a toast or perform other actions upon successful reporting
      },
      error => {
        console.error('Error reporting post:', error);
        // Optionally, you can show an error message or perform other actions upon unsuccessful reporting
      }
    );
  }
}




}

