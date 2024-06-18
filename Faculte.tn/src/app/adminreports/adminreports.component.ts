import { Component, OnInit } from '@angular/core';
import { ReportService } from '../Services/report.service';

@Component({
  selector: 'app-adminreports',
  templateUrl: './adminreports.component.html',
  styleUrls: ['./adminreports.component.css']
})
export class AdminreportsComponent implements OnInit {
  reportedPosts: any[] = [];
  reportedComments: any[] = [];

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    this.fetchReportedPosts();
    this.fetchReportedComments();
  }

  fetchReportedPosts() {
    this.reportService.getReportedPosts().subscribe(
      (posts: any[]) => {
        this.reportedPosts = posts;
        console.log('Reported posts:', posts); // Adding console log here

      },
      error => {
        console.error('Error fetching reported posts:', error);
      }
    );
  }

  fetchReportedComments() {
    this.reportService.getReportedComments().subscribe(
      (comments: any[]) => {
        this.reportedComments = comments;
        console.log('Reported comments:', comments); // Adding console log here

      },
      error => {
        console.error('Error fetching reported comments:', error);
      }
    );
  }

  deleteReportedPost(id: number) {
    this.reportService.deleteReportedPost(id).subscribe(
      () => {
        // Post deleted successfully, you might want to update the UI accordingly
        this.fetchReportedPosts(); // Refresh the list of reported posts
      },
      error => {
        console.error('Error deleting reported post:', error);
      }
    );
  }

  deleteReportedComment(id: number) {
    this.reportService.deleteReportedComment(id).subscribe(
      () => {
        // Comment deleted successfully, you might want to update the UI accordingly
        this.fetchReportedComments(); // Refresh the list of reported comments
      },
      error => {
        console.error('Error deleting reported comment:', error);
      }
    );
  }
}
