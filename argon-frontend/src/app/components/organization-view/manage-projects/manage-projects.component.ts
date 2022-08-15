import { ProjectDialogComponent } from './project-dialog/project-dialog.component';
import { ProjectService } from './../../../services/project.service';
import { ConfirmDialogComponent } from './../../confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Observable, Subscription } from 'rxjs';
import { OrganizationService } from 'src/app/services/organization.service';
import { Subproject } from './../../../models/subproject.model';
import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { Project } from 'src/app/models/project.model';
import { FlatTreeControl } from '@angular/cdk/tree';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';
import { Organization } from 'src/app/models/organization.model';

interface ProjectTreeNode {
  expandable: boolean;
  name: string;
  level: number;
  originalData: Project | Subproject;
}

@Component({
  selector: 'arg-manage-projects',
  templateUrl: './manage-projects.component.html',
  styleUrls: ['./manage-projects.component.css']
})
export class ManageProjectsComponent implements OnInit, OnDestroy {
  @Input() organization: Observable<Organization>;
  organizationSubscription: Subscription;

  private _transformer = (node: any, level: number) => {
    return {
      expandable: !!node.organizationId,
      name: node.name,
      level: level,
      originalData: node
    };
  };
  treeControl = new FlatTreeControl<ProjectTreeNode>(
    node => node.level,
    node => node.expandable,
  );
  treeFlattener = new MatTreeFlattener(
    this._transformer,
    node => node.level,
    node => node.expandable,
    node => this.getNodeChildren(node),
  );
  getNodeChildren = (node: Project | Subproject): Subproject[] => {
    if (node instanceof Subproject) {
      return [];
    }
    if (node.subprojects) {
      return node.subprojects.filter(subproject => subproject.deleted !== true);
    }
    return [];
  }
  projectTree = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);
  hasChild = (_: number, node: ProjectTreeNode) => node.expandable;
  organizationId: number;

  constructor(private dialog: MatDialog, private projectService: ProjectService) { }

  ngOnInit(): void {
    this.organizationSubscription = this.organization.subscribe(organization => {
      this.organizationId = organization.id;
      this.createTreeData();
    });
  }

  ngOnDestroy(): void {
    this.organizationSubscription.unsubscribe();
  }

  createTreeData() {
    this.projectService.getProjectsForOrganization(this.organizationId).subscribe(
      (projects) => this.projectTree.data = projects.filter(project => project.deleted !== true).sort((a, b) => a.id - b.id)
    );
  }

  removeProject(node: ProjectTreeNode, isChild: boolean) {
    if (isChild) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: {
          header: `Delete subproject`,
          content: `Do you want to delete subproject ${node.name}?`,
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response) {
          this.projectService.deleteSubproject(node.originalData as Subproject).subscribe(
              () => this.createTreeData()
            );
        }
      });
    } else {
      const project = node.originalData as Project;
      const subprojectCount = project.subprojects ? project.subprojects.length : 0;
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: {
          header: `Delete project`,
          content: `Do you want to delete project ${node.name} and all of its ${subprojectCount} subprojects?`,
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response) {
          this.projectService.deleteProject(node.originalData as Project).subscribe(
            () => this.createTreeData()
          );
        }
      });
    }
  }

  editProject(node: ProjectTreeNode, isChild: boolean) {
    if (isChild) {
      const dialogRef = this.dialog.open(ProjectDialogComponent, {
        data: {
          type: 'subproject',
          edit: true,
          project: node.originalData
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response && response.save) {
          this.projectService.updateSubproject(response.data).subscribe(
            () => this.createTreeData()
          );
        }
      });
    } else {
      const dialogRef = this.dialog.open(ProjectDialogComponent, {
        data: {
          type: 'project',
          edit: true,
          project: node.originalData
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response && response.save) {
          this.projectService.updateProject(response.data).subscribe(
            () => this.createTreeData()
          );
        }
      });
    }
  }

  addProject(node: ProjectTreeNode, isChild: boolean) {
    if (isChild) {
      const dialogRef = this.dialog.open(ProjectDialogComponent, {
        data: {
          type: 'subproject',
          edit: false,
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response && response.save) {
          console.log(node.originalData);
          response.data.projectId = (node.originalData as Project).id;
          this.projectService.createSubproject(response.data).subscribe(
            () => this.createTreeData()
          );
        }
      });
    } else {
      const dialogRef = this.dialog.open(ProjectDialogComponent, {
        data: {
          type: 'project',
          edit: false,
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response && response.save) {
          response.data.organizationId = this.organizationId;
          this.projectService.createProject(response.data).subscribe(
            () => this.createTreeData()
          );
        }
      });
    }
  }

}
