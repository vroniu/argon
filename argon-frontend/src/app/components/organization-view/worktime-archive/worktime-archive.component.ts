import { MatDialog } from '@angular/material/dialog';
import { WorktimeService } from './../../../services/worktime.service';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Worktime } from 'src/app/models/worktime.model';
import { Project } from 'src/app/models/project.model';
import { EditWorktimeDialogComponent } from '../edit-worktime-dialog/edit-worktime-dialog.component';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component';
import { Observable, Subscription } from 'rxjs';
import { Organization } from 'src/app/models/organization.model';

@Component({
  selector: 'arg-worktime-archive',
  templateUrl: './worktime-archive.component.html',
  styleUrls: ['./worktime-archive.component.css']
})
export class WorktimeArchiveComponent implements OnInit, OnDestroy {
  @Input() organization: Observable<Organization>;
  organizationSubscription: Subscription;

  projects: Project[];
  organizationId: number;

  rangeStart: Date;
  rangeEnd: Date;
  worktimes: Worktime[];

  constructor(private worktimeService: WorktimeService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.organizationSubscription = this.organization.subscribe(organization => {
      this.organizationId = organization.id;
      this.projects = organization.projects;
    });
  }

  ngOnDestroy(): void {
    this.organizationSubscription.unsubscribe();
  }

  getWorktimesAtRange() {
    this.worktimeService.getWorktimesAtDateRange(this.rangeStart, this.rangeEnd, this.organizationId).subscribe(
      worktimes => this.worktimes = worktimes
    );
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
        this.worktimeService.updateWorktime(response.data).subscribe(
          (updatedWorktime) => {
            this.worktimes = this.worktimes
              .map(worktime => worktime.id === updatedWorktime.id ? updatedWorktime : worktime);
          }
        );
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
        this.worktimeService.deleteWorktime(worktime).subscribe(
          () => {
            this.worktimes = this.worktimes
              .filter(item => item.id !== worktime.id);
          }
        );
      }
    });
  }

}
