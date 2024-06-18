import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-share-modal',
  templateUrl: './share-modal.component.html',
  styleUrls: ['./share-modal.component.css']
})
export class ShareModalComponent {
  additionalContent: string = '';

  constructor(
    public dialogRef: MatDialogRef<ShareModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { postId: number }
  ) {}

  onNoClick(): void {
    this.dialogRef.close();
  }

  sharePost(): void {
    this.dialogRef.close(this.additionalContent);
  }
}
