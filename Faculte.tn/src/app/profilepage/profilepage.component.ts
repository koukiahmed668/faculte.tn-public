import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UserService } from '../Services/user.service';
import { PostService } from '../Services/post.service';
import { FriendshipService } from '../Services/friendship.service';
import { ThemeService } from '../Services/theme.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { ChatDialogComponent } from '../chat-dialog/chat-dialog.component';
import { Router } from '@angular/router';
import { SpecialtyService } from '../Services/specialty.service';
import { MajorService } from '../Services/major.service';
@Component({
  selector: 'app-profilepage',
  templateUrl: './profilepage.component.html',
  styleUrl: './profilepage.component.css'
})
export class ProfilepageComponent implements OnInit {

  toggleDarkMode(): void {
    this.themeService.toggleDarkMode();
  }

  loggedInUserId: number | null = null;
  showSubjectList: boolean = false;
  subjects: any[] = [];
  selectedSubjectId: number | null = null;
  postContent: string = '';
  selectedFiles: File[] = [];
  userId!: number;
  posts: any[] = [];
  sharedPosts: any[] = [];
  post: any;
  newContent: string = '';
  newFiles: File[] = [];
  userDetails: any = {};
  friendshipStatus: { status: 'none' | 'sent' | 'received' |'friends' } = { status: 'none' };
  showNotification: boolean = false;
  showLanguageSwitcherPopup: boolean = false;
  showChatList: boolean = false;
  editedUserInfo: any = {}; // Object to hold the edited user information
  isEditFormOpen: boolean = false; // Flag to indicate if the edit form is open
  specialties: any[] = [];


  constructor(
    private friendshipService: FriendshipService,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer,
    private userService: UserService,
    private postService: PostService,
    private router:Router,
    private themeService: ThemeService,
    private specialtyService:SpecialtyService,
    private majorService:MajorService
  ) { }

  ngOnInit() {
    this.fetchLoggedInUserId();
    this.fetchSubjects();
    this.route.params.subscribe(params => {
      this.userId = params['userId'];
      this.userService.getUserDetails(this.userId).subscribe(
        userDetails => {
          this.userDetails = userDetails;
          console.log('details', userDetails);
          this.fetchPostsForProfileUser();
          this.fetchSharedPosts();
          this.fetchMajorByUserId();

        },
        error => {
          console.error('Error fetching user details:', error);
        }
      );
    });

    this.friendshipStatus = { status: 'none' };
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
        this.fetchFriendshipStatus();
        this.fetchPostsForProfileUser();
        this.fetchSharedPosts();
        this.fetchMajorByUserId();
      },
      error => {
        console.error('Error fetching logged-in user ID:', error);
      }
    );
  }

  fetchFriendshipStatus(): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
        this.friendshipService.getFriendshipStatus(this.loggedInUserId, this.userId).subscribe(
            (response: any) => {
                if (typeof response === 'string') {
                    this.friendshipStatus.status = response as 'none' | 'sent' | 'received' | 'friends';
                } else if (response && typeof response === 'object' && 'status' in response) {
                    const status = response.status;
                    if (typeof status === 'string' && ['none', 'sent', 'received', 'friends'].includes(status)) {
                        this.friendshipStatus.status = status as 'none' | 'sent' | 'received' | 'friends';
                    } else {
                        console.error('Invalid friendship status:', status);
                    }
                } else {
                    console.error('Invalid friendship status response:', response);
                }
                console.log('Friendship status:', this.friendshipStatus.status); // Added console log
            },
            error => {
                console.error('Error fetching friendship status:', error);
            }
        );
    } else {
        console.error('Logged-in user ID or profile user ID is not available');
    }
}



  sendFriendRequest(): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
      this.friendshipService.sendFriendRequest(this.loggedInUserId, this.userId).subscribe(
        response => {
          console.log('Friend request sent successfully:', response);
          // Fetch friendship status again after sending the request
          this.fetchFriendshipStatus();
        },
        error => {
          console.error('Error sending friend request:', error);
        }
      );
    } else {
      console.error('Logged-in user ID or profile user ID is not available');
    }
  }
  
  
  cancelFriendRequest(): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
      this.friendshipService.cancelFriendRequest(this.loggedInUserId, this.userId).subscribe(
        response => {
          console.log('Friend request canceled successfully:', response);
          // Optionally, you can update the UI or perform other actions after canceling the request
          this.fetchFriendshipStatus();

        },
        error => {
          console.error('Error canceling friend request:', error);
        }
      );
    } else {
      console.error('Logged-in user ID or profile user ID is not available');
    }
  }
  
  acceptFriendRequest(userId: number): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
      this.friendshipService.acceptFriendRequest(userId, this.loggedInUserId).subscribe(
        response => {
          console.log('Friend request accepted successfully:', response);
          // Optionally, you can update the UI or perform other actions after accepting the request
          this.fetchFriendshipStatus();
        },
        error => {
          console.error('Error accepting friend request:', error);
        }
      );
    } else {
      console.error('Logged-in user ID or profile user ID is not available');
    }
  }
  
  rejectFriendRequest(userId: number): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
      this.friendshipService.rejectFriendRequest(userId, this.loggedInUserId).subscribe(
        response => {
          console.log('Friend request declined successfully:', response);
          // Optionally, you can update the UI or perform other actions after declining the request
          this.fetchFriendshipStatus();
        },
        error => {
          console.error('Error declining friend request:', error);
        }
      );
    } else {
      console.error('Logged-in user ID or profile user ID is not available');
    }
  }
  
  deleteFriendship(): void {
    if (this.loggedInUserId !== null && this.userId !== null) {
      this.friendshipService.deleteFriendship(this.loggedInUserId, this.userId).subscribe(
        response => {
          console.log('Friendship deleted successfully:', response);
          // Optionally, you can update the UI or perform other actions after deleting the friendship
          // For example, you may want to fetch the friendship status again
          this.fetchFriendshipStatus();
        },
        error => {
          console.error('Error deleting friendship:', error);
        }
      );
    } else {
      console.error('Logged-in user ID or profile user ID is not available');
    }
  }
  

  fetchSubjects(): void {
    this.userService.getUserId().subscribe(
      userId => {
        this.userService.getUserSubjects().subscribe(
          (subjects: any[]) => {
            this.subjects = subjects;
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

  fetchSharedPosts(): void {
    this.postService.getSharedPosts(this.userId).subscribe(
      sharedPosts => {
        this.sharedPosts = sharedPosts;
        console.log('sharedposts',sharedPosts);
      },
      error => {
        console.error('Error fetching shared posts:', error);
      }
    );
  }

  fetchPostsForProfileUser(): void {
    if (this.selectedSubjectId !== null) {
      this.postService.getPostsBySubjectId(this.userId).subscribe(
        posts => {
          this.posts = posts;
          this.processPosts();
        },
        error => {
          console.error('Error fetching posts:', error);
        }
      );
    } else {
      this.postService.getPostsByUserId(this.userId).subscribe(
        posts => {
          this.posts = posts;
          this.processPosts();
        },
        error => {
          console.error('Error fetching posts:', error);
        }
      );
    }
  }

  processPosts(): void {
    this.posts.sort((a, b) => {
      return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
    });

    this.posts.forEach(post => {
      this.userService.getUserById().subscribe(
        user => {
          post.authorFirstName = user.firstName;
          post.authorLastName = user.lastName;
        },
        error => {
          console.error('Error fetching user information:', error);
        }
      );

      if (post.files && post.files.length > 0) {
        post.files.forEach((file: any) => {
          this.postService.getFileData(file.id).subscribe(
            (fileData: Blob) => {
              const reader = new FileReader();
              reader.onload = () => {
                file.fileUrl = reader.result as string;
              };
              reader.readAsDataURL(fileData);
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
    this.fetchPostsForProfileUser();
  }

  onFileSelected(event: any): void {
    this.selectedFiles = event.target.files;
  }

  createPost(): void {
    this.postService.createPost(this.postContent, this.selectedFiles, this.selectedSubjectId)
      .then(() => {
        this.fetchPostsForProfileUser();
        this.postContent = '';
        this.selectedFiles = [];
      })
      .catch(error => {
        console.error('Error creating post:', error);
      });
  }

  toggleMenu(postId: number): void {
    this.posts.forEach(post => {
      if (post.id === postId) {
        post.showMenu = !post.showMenu;
      } else {
        post.showMenu = false;
      }
    });
  }

  savePost(postId: number): void {
    const userId = this.loggedInUserId ?? 0;
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
    this.userService.getUserId().subscribe(
      userId => {
        this.postService.getBookmarkedPosts(userId).subscribe(
          bookmarkedPosts => {
            const fetchUserInfoPromises = bookmarkedPosts.map(post => {
              return this.userService.getUserById().toPromise();
            });
            Promise.all(fetchUserInfoPromises).then(users => {
              bookmarkedPosts.forEach((post, index) => {
                post.authorFirstName = users[index].firstName;
                post.authorLastName = users[index].lastName;
              });
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
    const userId = this.loggedInUserId ?? 0;
    this.postService.deletePost(postId, userId).subscribe(
      () => {
        console.log('Post deleted successfully');
        this.fetchPostsForProfileUser();
      },
      error => {
        console.error('Failed to delete post:', error);
        this.fetchPostsForProfileUser();
      }
    );
  }

  updatePost(postId: number, newContent: string, newFiles: File[]): void {
    const userId = this.loggedInUserId ?? 0;
    this.postService.updatePost(postId, userId, newContent, newFiles)
      .subscribe(
        (response: any) => {
          console.log('Post updated successfully:', response);
          this.newContent = '';
          this.newFiles = [];
          const updatedPost = this.posts.find(p => p.id === postId);
          if (updatedPost) {
            updatedPost.showEditForm = false;
          }
          this.fetchPostsForProfileUser();
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
    if (post.showEditForm === undefined) {
      post.showEditForm = false;
    }
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


  toggleNotification(): void {
    this.showNotification = !this.showNotification;
  }

  toggleChatList(): void {
    this.showChatList = !this.showChatList; // Toggle showChatList value
  }

  openEditForm(): void {
    this.editedUserInfo = { ...this.userDetails };
    this.isEditFormOpen = true;
  }

  closeEditForm(): void {
    this.isEditFormOpen = false;
  }

  updateUserInfo(): void {
    // Call the user service to update the user information
    this.userService.updateStudent(this.userDetails.id, this.editedUserInfo).subscribe(
      (response) => {
        console.log('User information updated successfully:', response);
        // Optionally, you can update the userDetails with the updated information
        this.userDetails = { ...this.editedUserInfo };
        this.closeEditForm(); // Close the edit form after successful update
      },
      (error) => {
        console.error('Error updating user information:', error);
      }
    );
  }

  getAllSpecialtiesByMajor(majorId: number): void {
    this.specialtyService.getAllSpecialtiesByMajor(majorId).subscribe(
      (specialties: any[]) => {
        this.specialties = specialties;
      },
      error => {
        console.error('Error fetching specialties:', error);
      }
    );
  }

  fetchMajorByUserId(): void {
    if (this.userId !== null) {
      this.majorService.getMajorIdByUserId(this.userId).subscribe(
        (majorId: number) => {
          this.getAllSpecialtiesByMajor(majorId); 
          console.log('Specialties:', this.specialties);
        },
        error => {
          console.error('Error fetching major ID:', error);
        }
      );
    } else {
      console.error('User ID is not available');
    }
  }
  
}
