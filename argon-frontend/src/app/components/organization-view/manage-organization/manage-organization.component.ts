import { OrganizationDialogComponent } from './../../organizations/organization-dialog/organization-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { OrganizationService } from 'src/app/services/organization.service';
import { Employee } from './../../../models/employee.model';
import { Organization } from './../../../models/organization.model';
import { Component, Input, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';

@Component({
  selector: 'arg-manage-organization',
  templateUrl: './manage-organization.component.html',
  styleUrls: ['./manage-organization.component.css']
})
export class ManageOrganizationComponent implements OnInit {
  @Input() organization: Observable<Organization>;
  organizationSubscription: Subscription;
  organizationId: number;
  organizationInfo: Organization;
  employees: Employee[] = [];

  constructor(private organizationService: OrganizationService, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.organizationSubscription = this.organization.subscribe(organization => {
      this.organizationId = organization.id;
      this.getOrganizationData();
    });
  }

  editOrganization(): void {
    const dialogRef = this.dialog.open(OrganizationDialogComponent, {
      data: this.organizationInfo
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data.save) {
        // UPDATE ORGANIZATION
      }
    });
  }

  private getOrganizationData(): void {
    this.organizationService.getOrganizationById(this.organizationId).subscribe(organization => {
      this.organizationInfo = organization;
    });
    this.organizationService.getOrganizationEmployees(this.organizationId).subscribe(employees => {
      this.employees = employees;
    });
  }

}
