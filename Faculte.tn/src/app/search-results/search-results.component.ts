import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ThemeService } from '../Services/theme.service';
import { Router } from '@angular/router';
import {UserService} from '../Services/user.service'
import {PostService} from '../Services/post.service'
import {ReportService} from '../Services/report.service'
import { ShareModalComponent } from '../share-modal/share-modal.component'; // Import your share modal component
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MatSnackBar } from '@angular/material/snack-bar'; // Import MatSnackBar
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { SearchService } from "../Services/search.service";

@Component({
  selector: 'app-search-results',
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.css'
})
export class SearchResultsComponent {
showLanguageSwitcherPopup: boolean = false;
showNotification: boolean = false;
showChatList: boolean = false;
loggedInUserId!:number;
posts: any[] = []; // Array to store posts
newContent: string = ''; // Declare newContent property
newFiles: File[] = []; // Declare newFiles property
selectedSubjectId: number | null = null;
query: string = '';
searchedUsers: any[] = []; // Define the searchedUsers property to hold the list of users


  constructor(private reportService:ReportService,
     private postService:PostService,
     private searchService:SearchService,
      private route: ActivatedRoute,
      private themeService:ThemeService,
      private router:Router,
      private sanitizer: DomSanitizer,
      private snackBar: MatSnackBar,
      private dialog: MatDialog,
      private userService:UserService) { }

      ngOnInit(): void {
        this.fetchLoggedInUserId();
        this.route.queryParams.subscribe(params => {
          this.query = params['query'];
          if (this.query.trim() !== '') {
            this.searchPosts(this.query);
            this.searchUsers(this.query);
          }
        });
      }

      searchPosts(query: string): void {
        this.searchService.searchPosts(query).subscribe(
          posts => {
            this.posts = posts;
            this.processPosts();
            console.log('posts',posts);
          },
          error => {
            console.error('Error searching posts:', error);
          }
        );
      }

      searchUsers(query: string): void {
        this.searchService.searchUsers(query).subscribe(
          users => {
            this.searchedUsers = users; // Assign the retrieved users to the searchedUsers property
          },
          error => {
            console.error('Error searching users:', error);
          }
        );
      }
      

      
    

toggleLanguageSwitcherPopup(): void {
  this.showLanguageSwitcherPopup = !this.showLanguageSwitcherPopup;
}
toggleNotification(): void {
  this.showNotification = !this.showNotification;
}

toggleChatList(): void {
  this.showChatList = !this.showChatList; // Toggle showChatList value
}
toggleDarkMode(): void {
  this.themeService.toggleDarkMode();
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
   },
    error => {
      console.error('Error fetching logged-in user ID:', error);
    }
  );
}



processPosts(): void {
  // Sort posts based on createdAt property (newest first)
  this.posts.sort((a, b) => {
    return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
  });

  // Fetch additional user information and file data for each post
  this.posts.forEach(post => {
    
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


toggleEdit(post: any): void {
  // Initialize the showEditForm property if it's undefined
  if (post.showEditForm === undefined) {
    post.showEditForm = false;
  }
  // Toggle the edit mode for the clicked post
  post.showEditForm = !post.showEditForm;
}

deletePost(postId: number): void {
  const userId = this.loggedInUserId ?? 0; // Use 0 as the default value if loggedInUserId is null
  this.postService.deletePost(postId, userId).subscribe(
    () => {
      console.log('Post deleted successfully');
      // Call fetchPosts again to update the posts list
      this.posts.forEach(post => post.showComments = false);
    },
    error => {
      console.error('Failed to delete post:', error);
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

}
