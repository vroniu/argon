import { Subproject } from 'src/app/models/subproject.model';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { Project } from 'src/app/models/project.model';

interface ProjectDialogComponentData {
  type: 'project' | 'subproject';
  edit: boolean,
  project?: Project | Subproject;
}

@Component({
  selector: 'arg-project-dialog',
  templateUrl: './project-dialog.component.html',
  styleUrls: ['./project-dialog.component.css']
})
export class ProjectDialogComponent implements OnInit {
  header: string = '';
  project: Project | Subproject;

  constructor(private dialogRef: MatDialogRef<ProjectDialogComponent>, @Inject(MAT_DIALOG_DATA) public data: ProjectDialogComponentData) { }

  ngOnInit(): void {
    if (this.data.edit) {
      this.header += 'Edit ';
      this.project = this.data.project;
    } else {
      this.header += 'Add '
      if (this.data.type === 'project') {
        this.project = new Project();
      } else {
        this.project = new Subproject();
      }
    }
    this.header += this.data.type;
  }

  onClose(save: boolean) {
    if (save) {
      this.dialogRef.close({
        save: true,
        data: this.project
      });
    } else {
      this.dialogRef.close({ save: false });
    }
  }

}
