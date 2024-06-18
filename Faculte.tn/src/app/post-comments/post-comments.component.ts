import { Component, Input, ChangeDetectorRef } from '@angular/core';
import { UserService } from '../Services/user.service';
import { PostService } from '../Services/post.service';
import { Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ReportService } from '../Services/report.service'; // Import the ReportService


@Component({
  selector: 'app-post-comments',
  templateUrl: './post-comments.component.html',
  styleUrl: './post-comments.component.css'
})
export class PostCommentsComponent {
  @Input() postId!: number; // Define postId as an input property
  comments: any[] = []; // Array to store comments
  newComment: string = ''; // Variable to store new comment text
  userId!: number
  isModalOpen: boolean = true; // Flag to control modal visibility
  loggedInUserId: number | null = null;

  constructor(private postService: PostService, private userService: UserService, private reportService: ReportService, private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    // Retrieve userId asynchronously
    this.userService.getUserId().subscribe(
      (userId: number) => {
        this.loggedInUserId = userId;
        this.loadComments(); // Load comments after userId is available
      },
      (error) => {
        console.error('Error retrieving userId:', error);
      }
    );
  }

  loadComments() {
    // Call your comment service to fetch comments by postId
    this.postService.getCommentsByPostId(this.postId).subscribe(
      (data: any[]) => {
        this.comments = data;
        console.log('comments',this.comments);
      },
      (error) => {
        console.error('Error fetching comments:', error);
      }
    );
  }

  addComment() {
    if (!this.loggedInUserId) {
      console.error('User ID is not available');
      return;
    }

    if (this.newComment.trim() === '') {
      return;
    }

    this.postService.addCommentToPost(this.postId, this.loggedInUserId, this.newComment).subscribe(
      (comment: any) => {
        this.loadComments();
        this.comments.push(comment);
        this.newComment = ''; 
        this.cdr.detectChanges(); 
        this.loadComments();
      },
      (error) => {
        console.error('Error adding comment:', error);
      }
    );
  }

  removeComment(commentId: number) {
    if (!this.loggedInUserId) {
      console.error('Logged-in user ID is not available');
      return;
    }
  
    // Call your comment service to remove the comment
    this.postService.removeCommentFromPost(this.postId, commentId, this.loggedInUserId).subscribe(
      () => {
        // Remove the comment from the list
        this.comments = this.comments.filter(comment => comment.id !== commentId);
      },
      (error) => {
        console.error('Error removing comment:', error);
      }
    );
  }
  
  updateComment(commentId: number, newContent: string) {
    if (!this.loggedInUserId) {
      console.error('Logged-in user ID is not available');
      return;
    }

    this.postService.updateCommentInPost(this.postId, commentId, this.loggedInUserId, newContent).subscribe(
      (updatedComment: any) => {
        // Find the index of the updated comment in the array
        const index = this.comments.findIndex(comment => comment.id === updatedComment.id);
        if (index !== -1) {
          // Update the comment in the array
          this.comments[index] = updatedComment;
        }
      },
      (error) => {
        console.error('Error updating comment:', error);
      }
    );
  }

  toggleEditMode(comment: any) {
    comment.editing = !comment.editing;
    comment.updatedContent = comment.content; // Initialize updatedContent with the current comment content
  }

  saveUpdatedComment(comment: any) {
    this.updateComment(comment.id, comment.updatedContent);
    comment.editing = false; // Exit editing mode after saving
  }
  
  closeModal() {
    this.isModalOpen = false; 
  }


  reportComment(commentId: number) {
    const reason = prompt('Please provide a reason for reporting this comment:');
    if (!reason) {
      return;
    }

    this.reportService.reportComment(commentId, reason).subscribe(
      () => {
        alert('Comment reported successfully');
      },
      (error) => {
        console.error('Error reporting comment:', error);
        alert('An error occurred while reporting the comment');
      }
    );
  }

}
