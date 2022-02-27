import { WorktimeFormComponent } from './../worktime-form/worktime-form.component';
import { EditWorktimeDialogComponent } from './../edit-worktime-dialog/edit-worktime-dialog.component';
import { ConfirmDialogComponent, ConfirmDialogData } from './../../confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { FormGroup } from '@angular/forms';
import { WorktimeService } from '../../../services/worktime.service';
import { Subproject } from '../../../models/subproject.model';
import { Component, Input, OnInit, Output, ViewChild, EventEmitter, OnDestroy } from '@angular/core';
import { Project } from 'src/app/models/project.model';
import { Worktime } from 'src/app/models/worktime.model';
import { DateTime } from 'luxon';
import { Subscription, Observable } from 'rxjs';
import { Organization } from 'src/app/models/organization.model';

@Component({
  selector: 'arg-submit-worktime',
  templateUrl: './submit-worktime.component.html',
  styleUrls: ['./submit-worktime.component.css']
})
export class SubmitWorktimeComponent implements OnInit, OnDestroy {
  @ViewChild('worktimeForm') worktimeForm: WorktimeFormComponent;
  @Input() organization: Observable<Organization>;
  organizationSubscription: Subscription;
  worktimesUpdatedSubscription: Subscription;

  projects: Project[] = [];
  organizationId: number;
  selectedProject: Project | null = null;
  selectedSubprojet: Subproject | null = null;

  todayWorktimes: Worktime[] = [];

  constructor(private worktimeService: WorktimeService, private dialog: MatDialog) { }

  ngOnDestroy(): void {
    this.worktimesUpdatedSubscription.unsubscribe();
    this.organizationSubscription.unsubscribe();
  }

  ngOnInit(): void {
    this.organizationSubscription = this.organization.subscribe(organization => {
      this.organizationId = organization.id;
      this.projects = organization.projects;
      this.worktimeForm.setProjects(this.projects);
      this.getWorktimesForToday();
    });
    this.worktimesUpdatedSubscription = this.worktimeService.getWorktimesUpdated().subscribe(
      (date) => {
        const ddate = DateTime.fromJSDate(new Date(date));
        const today = DateTime.local();
        if (ddate.hasSame(today, 'day')) {
          this.getWorktimesForToday();
        }
      }
    );
  }

  onWorktimeSave(worktime: Worktime) {
      this.worktimeService.createWorktime(worktime).subscribe();
  }

  onWorktimeEdit(worktime: Worktime) {
    const dialogRef = this.dialog.open(EditWorktimeDialogComponent, {
      data: {
        worktime: JSON.parse(JSON.stringify(worktime)),
        projects: this.projects
      }
    });
    dialogRef.afterClosed().subscribe((response) => {
      if (response && response.save) {
        this.worktimeService.updateWorktime(response.data).subscribe();
      }
    });
  }

  onWorktimeDelete(worktime: Worktime) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        header: 'Confirm delete',
        content: 'Do you want to delete this worktime?'
      }
    });
    dialogRef.afterClosed().subscribe((response) => {
      if (response) {
        this.worktimeService.deleteWorktime(worktime).subscribe();
      }
    });
  }

  getWorktimesForToday() {
    this.worktimeService.getWorktimesAtDay(new Date(), this.organizationId).subscribe((worktimes) => {
      this.todayWorktimes = worktimes.sort((a, b) => a.id - b.id);
    });
  }

}
