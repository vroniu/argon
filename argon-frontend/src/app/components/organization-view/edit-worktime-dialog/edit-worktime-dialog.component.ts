import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Project } from 'src/app/models/project.model';
import { Worktime } from 'src/app/models/worktime.model';

export interface EditWorktimeDialogData {
  worktime: Worktime;
  projects: Project[];
}

@Component({
  selector: 'arg-edit-worktime-dialog',
  templateUrl: './edit-worktime-dialog.component.html',
  styleUrls: ['./edit-worktime-dialog.component.css']
})
export class EditWorktimeDialogComponent implements OnInit {

  constructor(private dialogRef: MatDialogRef<EditWorktimeDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: EditWorktimeDialogData) { }

  ngOnInit(): void {
  }

  onClose(worktime: Worktime) {
    if (worktime) {
      this.dialogRef.close({
        save: true,
        data: worktime
      });
    } else {
      this.dialogRef.close({ save: false });
    }
  }

}
