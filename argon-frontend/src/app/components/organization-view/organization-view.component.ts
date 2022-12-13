import { ConfirmDialogComponent } from './../confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Employee } from 'src/app/models/employee.model';
import { Organization } from 'src/app/models/organization.model';
import { OrganizationService } from 'src/app/services/organization.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
  selector: 'arg-organization-view',
  templateUrl: './organization-view.component.html',
  styleUrls: ['./organization-view.component.css']
})
export class OrganizationViewComponent implements OnInit {
  view: 'owner' | 'employee' = 'employee';
  organization: Organization;
  employeeInfo: Employee;
  organizationSubject = new Subject<Organization>();

  constructor(private router: Router, private route: ActivatedRoute,
    private organizationService: OrganizationService, private dialog: MatDialog, private changeDetector: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.organizationService.getOrganizationById(params.id).subscribe(organization => {
        this.organization = organization;
        this.organizationSubject.next(organization);
        this.organizationService.getOrganizationEmployeeInfo(this.organization.id).subscribe(
          employeeInfo => this.employeeInfo = employeeInfo
        );
        // Subscribe to topic to refresh organization info
        this.organizationService.getOrganizationsUpdated().subscribe(id => {
          if (id === this.organization.id) {
            this.organizationService.getOrganizationById(id).subscribe(updatedOrganization => {
              this.organization = updatedOrganization;
              this.organizationSubject.next(updatedOrganization);
            });
          }
        })
      });
    });
    this.route.queryParams.subscribe(queryParams => {
      if (queryParams.owner === 'true') {
        this.view = 'owner'
      }
    });
  }

  goBack() {
    this.router.navigate(['/main/organizations']);
  }

  leaveOrganization() {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        header: 'Leave organization',
        content: `Do you want to leave organization ${this.organization.name}?`,
      }
    });
  }

}
