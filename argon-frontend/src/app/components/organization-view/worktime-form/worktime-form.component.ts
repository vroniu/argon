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

  availableProjects: Project[];
  availableSubprojects: Map<number, Subproject[]> = new Map();
  selectedProject: Project | null;
  selectedSubprojet: Subproject | null;

  constructor() { }

  ngOnInit(): void {
    if (this.edit) {
      this.setProjects(this.projects);
      const worktimeProject = this.projects.find(project => this.worktime.subproject.projectId === project.id);
      const worktimeSubproject = worktimeProject.subprojects.find(subproject => subproject.id === this.worktime.subproject.id);
      if (worktimeProject.deleted) {
        this.availableProjects.push(worktimeProject);
        this.availableSubprojects.set(worktimeProject.id, worktimeProject.subprojects);
      } else if (worktimeSubproject.deleted) {
        this.availableSubprojects.set(worktimeProject.id, [...this.availableSubprojects.get(worktimeProject.id), worktimeSubproject]);
      }
      this.worktime.day = new Date(this.worktime.day);
      this.selectedProject = this.availableProjects.find(project => project.id === worktimeProject.id);
      this.selectedSubprojet = this.availableSubprojects.get(this.selectedProject.id)
        .find(subproject => subproject.id === worktimeSubproject.id)
    } else {
      this.resetWorktimeForm();
    }
  }

  setProjects(projects: Project[]) {
    this.availableProjects = projects.filter(project => project.deleted !== true);
    this.availableProjects.forEach(project => {
      this.availableSubprojects.set(
        project.id,
        project.subprojects.filter(subproject => subproject.deleted !== true)
      );
    });
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
