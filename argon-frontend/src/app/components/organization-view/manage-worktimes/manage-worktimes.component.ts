import { MatDialog } from '@angular/material/dialog';
import { WorktimeService } from './../../../services/worktime.service';
import { ProjectService } from './../../../services/project.service';
import { Worktime } from './../../../models/worktime.model';
import { Subproject } from './../../../models/subproject.model';
import { Project } from './../../../models/project.model';
import { Employee } from './../../../models/employee.model';
import { Organization } from './../../../models/organization.model';
import { Observable, Subscription } from 'rxjs';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { EditWorktimeDialogComponent } from '../edit-worktime-dialog/edit-worktime-dialog.component';
import { ConfirmDialogComponent } from '../../confirm-dialog/confirm-dialog.component';
import * as _ from 'lodash';

@Component({
  selector: 'arg-manage-worktimes',
  templateUrl: './manage-worktimes.component.html',
  styleUrls: ['./manage-worktimes.component.css']
})
export class ManageWorktimesComponent implements OnInit, OnDestroy {
  @Input() organization: Observable<Organization>;
  organizationSubscription: Subscription;
  projectsSubscription: Subscription;
  organizationId: number;
  employeeList: Employee[];
  projectList: Project[];
  availableSubprojectList: Subproject[];

  selectedEmployees: number[] = [];
  selectedProjects: number[] = [];
  selectedSubprojects: number[] = [];
  rangeStart: Date;
  rangeEnd: Date;

  worktimes: Worktime[] = [];

  constructor(private projectService: ProjectService, private worktimeService: WorktimeService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.organizationSubscription = this.organization.subscribe(organization => {
      this.organizationId = organization.id;
      this.employeeList = organization.employees;
      this.projectList = organization.projects;
    });
    this.projectsSubscription = this.projectService.getProjectsUpdated().subscribe(organizationId => {
      if (this.organizationId = organizationId) {
        this.projectService.getProjectsForOrganization(this.organizationId).subscribe(projects => {
          this.projectList = projects;
          this.availableSubprojectList = this.mapProjectsToSubprojectsList(
            this.projectList.filter(project => this.selectedProjects.includes(project.id))
          );
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.organizationSubscription.unsubscribe();
    this.projectsSubscription.unsubscribe();
  }

  onSelectedProjectsChange(selectedProjectsId: number[]) {
    const selectedProjects = this.projectList.filter(project => selectedProjectsId.includes(project.id));
    const availableSubprojectsFlatmap = this.mapProjectsToSubprojectsList(selectedProjects);
    this.selectedSubprojects = this.selectedSubprojects.filter(subprojectId => availableSubprojectsFlatmap.some(subproject => subproject.id === subprojectId));
    this.availableSubprojectList = availableSubprojectsFlatmap.filter(subproject => selectedProjectsId.includes(subproject.projectId));
  }

  onSearchWorktimes() {
    this.worktimeService.getFilteredWorktimesForOrganization(
      this.rangeStart, this.rangeEnd, this.selectedEmployees, this.selectedSubprojects, this.organizationId
    ).subscribe((worktimes) => {
      this.worktimes = worktimes;
    });
  }

  onWorktimeEdit(worktime: Worktime) {
    const dialogRef = this.dialog.open(EditWorktimeDialogComponent, {
      data: {
        worktime: _.cloneDeep(worktime),
        projects: this.projectList
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

  private mapProjectsToSubprojectsList(projects: Project[]): Subproject[] {
    const subprojectFlatmap = projects.map(project => project.subprojects);
    if (subprojectFlatmap.length === 0) {
      return [];
    }
    return projects.map(project => project.subprojects)
      .reduce((a,b) => {
        if (!a) {
          a = [];
        }
        if (!b) {
          b = [];
        }
        return a.concat(b);
      });
  }

}
