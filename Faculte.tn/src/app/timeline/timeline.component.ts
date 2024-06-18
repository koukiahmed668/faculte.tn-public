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


@Component({
  selector: 'app-timeline',
  templateUrl: './timeline.component.html',
  styleUrls: ['./timeline.component.css'],
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
export class TimelineComponent implements OnInit {

  toggleDarkMode(): void {
    this.themeService.toggleDarkMode();
  }

  loggedInUserId: number | null = null;
  showSubjectList: boolean = false;
  subjects: any[] = [];
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
  constructor(private reportService: ReportService, private friendshipService:FriendshipService ,private paidCourseService: PaidCourseService,private snackBar: MatSnackBar, private dialog: MatDialog, private router: Router, private sanitizer: DomSanitizer, private http: HttpClient, private userService: UserService, private postService: PostService, private themeService: ThemeService) { }

  

  ngOnInit() {
    this.startCarousel();
    this.fetchSubjects();
    this.fetchLoggedInUserId();
    this.fetchPosts(); // Fetch posts on component initialization
    this.fetchSelectedCourse(); // Replace 'courseId' with the ID of the selected course
    this.posts.forEach(post => post.showComments = false);


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
  

  fetchSubjects(): void {
    // Fetch the user's ID
    this.userService.getUserId().subscribe(
      userId => {
        // Once you have the user's ID, call the service method to fetch subjects
        this.userService.getUserSubjects().subscribe(
          (subjects: any[]) => {
            this.subjects = subjects;
            console.log('Subjects:', this.subjects); 
          },
          error => {
            console.error('Error fetching subjects:', error);
          }
        );
      },
      error => {
        console.error('Error fetching user ID:', error);
      }
    );
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
      this.postService.getAllPostsBySpecialty().subscribe(
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

  toggleSubjectList(): void {
    this.showSubjectList = !this.showSubjectList;
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
            // Fetch additional user information for each bookmarked post
            const fetchUserInfoPromises = bookmarkedPosts.map(post => {
              // Check if post.authorId is valid before making the request
              if (post.authorId) {
                return this.userService.getUserDetails(post.authorId).toPromise();
              } else {
                return Promise.resolve(null); // Return a resolved promise if authorId is undefined
              }
            });
            Promise.all(fetchUserInfoPromises).then(users => {
              // Assign author details to each bookmarked post
              bookmarkedPosts.forEach((post, index) => {
                post.author = users[index]; // Assuming you have an `author` property in your post object
              });
              // Update the posts array with bookmarked posts
              this.posts = bookmarkedPosts;
            }).catch(error => {
              console.error('Error fetching user information:', error);
            });
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
      // Process formations after fetching data
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

