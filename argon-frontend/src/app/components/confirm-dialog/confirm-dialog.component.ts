import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface ConfirmDialogData {
  header: string,
  content: string,
  confirmButton?: string;
  cancelButton?: string;
}

@Component({
  selector: 'arg-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.css']
})
export class ConfirmDialogComponent {

  constructor(private dialogRef: MatDialogRef<ConfirmDialogComponent, boolean>, @Inject(MAT_DIALOG_DATA) public data: ConfirmDialogData ) { }

  onClose(confirm: boolean) {
    this.dialogRef.close(confirm);
  }

}
