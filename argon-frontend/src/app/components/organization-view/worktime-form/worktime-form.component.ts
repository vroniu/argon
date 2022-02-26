import { Worktime } from 'src/app/models/worktime.model';
import { Component, OnInit, Input, EventEmitter, Output, ViewChild } from '@angular/core';
import { Project } from 'src/app/models/project.model';
import { Subproject } from 'src/app/models/subproject.model';
import { FormGroup } from '@angular/forms';

const flatMap = (f: any, xs: any) =>
  xs.reduce((r: any, x: any) => r.concat(f(x)), [])

@Component({
  selector: 'arg-worktime-form',
  templateUrl: './worktime-form.component.html',
  styleUrls: ['./worktime-form.component.css']
})
export class WorktimeFormComponent implements OnInit {
  @Input() worktime: Worktime = new Worktime();
  @Input() projects: Project[] = [];
  @Input() edit = false;
  @Output() worktimeSaved = new EventEmitter<Worktime>();
  @ViewChild('worktimeForm') worktimeForm: FormGroup;

  selectedProject: Project | null;
  selectedSubprojet: Subproject | null;

  constructor() { }

  ngOnInit(): void {
    if (this.edit) {
      this.worktime.day = new Date(this.worktime.day);
      this.selectedSubprojet = this.projects
        .map(project => project.subprojects).reduce((arr, elem) => arr.concat(elem), [])
        .find(subproject => subproject.id === this.worktime.subproject.id);
      this.selectedProject = this.projects.find(project => project.id === this.selectedSubprojet.projectId);
    } else {
      this.resetWorktimeForm();
    }
  }

  onWorktimeSubmit() {
    if (this.worktimeForm.valid && this.selectedProject !== null && this.selectedSubprojet !== null && this.worktime.hours !== null) {
      this.worktime.subproject = this.selectedSubprojet;
      this.worktimeSaved.emit(this.worktime);
      if (!this.edit) {
        this.resetWorktimeForm();
      }
    }
  }

  onCancel() {
    this.worktimeSaved.emit(null);
  }

  resetWorktimeForm() {
    this.worktime.day = new Date();
    this.worktime.hours = null;
    this.worktime.comment = null;
    this.selectedProject = null;
    this.selectedSubprojet = null;
  }

}
