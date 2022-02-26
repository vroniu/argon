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
  getNodeChildren = (node: Project | Subproject ): Subproject[] => {
    if (node instanceof Subproject) {
      return [];
    }
    if (node.subprojects) {
      return node.subprojects;
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
      this.projectTree.data = organization.projects;
    });
  }

  ngOnDestroy(): void {
    this.organizationSubscription.unsubscribe();
  }

  removeNode(node: ProjectTreeNode, isChild: boolean) {
    if (isChild) {
      const dialogRef = this.dialog.open(ConfirmDialogComponent, {
        data: {
          header: `Delete subproject`,
          content: `Do you want to delete subproject ${node.name}?`,
        }
      });
      dialogRef.afterClosed().subscribe((response) => {
        if (response) {
          this.projectService.deleteSubproject(node.originalData.id).subscribe();
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
          this.projectService.deleteProject(node.originalData.id).subscribe();
        }
      });
    }
  }

  editNode(node: ProjectTreeNode) {

  }

}
