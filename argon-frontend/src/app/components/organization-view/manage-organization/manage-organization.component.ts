import { AddEmployeeDialogComponent, AddEmployeeDialogData } from './add-employee-dialog/add-employee-dialog.component';
import { ConfirmDialogComponent, ConfirmDialogData } from './../../confirm-dialog/confirm-dialog.component';
import { EmployeeDialogComponent } from './employee-dialog/employee-dialog.component';
import { OrganizationDialogComponent, OrganizationDialogData } from './../../organizations/organization-dialog/organization-dialog.component';
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
    this.organizationSubscription = this.organization.subscribe(async organization => {
      this.organizationId = organization.id;
      this.getOrganizationData()
    });
  }

  editOrganization(): void {
    const dialogRef = this.dialog.open(OrganizationDialogComponent, {
      data: {
        editMode: true,
        organization: this.organizationInfo
      } as OrganizationDialogData
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data.save) {
        this.organizationService.updateOrganization(data.organization).subscribe(organization => {
          console.log(organization);
        });
      }
    });
  }

  deleteOrganization(): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        header: 'Delete organization',
        content: `Are you sure you want to delete organization ${this.organizationInfo.name}?`,
        serious: true,
        seriousWarning: 'This will delete all the organization\'s projects as well as worktimes.',
        seriousConfirmWord: this.organizationInfo.name
      } as ConfirmDialogData
    })
  }

  editEmployee(employee: Employee): void {
    const dialogRef = this.dialog.open(EmployeeDialogComponent, {
      data: employee
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data.save) {
        this.organizationService.updateEmployeeInfoForOrganization(this.organizationInfo.id, data.employee).subscribe((employee) => {
          this.getEmployeesData();
        });
      }
    });
  }

  deleteEmployee(employee: Employee): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        header: 'Remove employee',
        content: `Are you sure you want to remove employee ${employee.firstName} ${employee.lastName} from organization ${this.organizationInfo.name}?`,
      } as ConfirmDialogData
    });
    dialogRef.afterClosed().subscribe(confirm => {
      if (confirm) {
        this.organizationService.deleteEmployeeFromOrganization(this.organizationInfo.id, employee.id).subscribe(() => {
          this.getEmployeesData();
        });
      }
    })
  }

  addEmployee(): void {
    const dialogRef = this.dialog.open(AddEmployeeDialogComponent, {
      data: {
        organizationId: this.organizationInfo.id
      } as AddEmployeeDialogData
    });
  }

  private getOrganizationData(): void {
    this.organizationService.getOrganizationById(this.organizationId).subscribe(organization => {
      this.organizationInfo = organization;
      this.getEmployeesData();
    });
  }

  private getEmployeesData(): void {
    this.organizationService.getOrganizationEmployees(this.organizationId).subscribe(employees => {
      this.employees = employees;
      this.employees.forEach(employee => {
        if (this.organizationInfo.owners.some(owner => owner.id === employee.id)) {
          employee.isOwner = true;
        }
      });
      this.employees = this.employees.sort(this.sortEmployeesByOwner)
    });
  }

  private sortEmployeesByOwner(a: Employee, b: Employee) {
    if (a.isOwner) {
      if (b.isOwner) {
        return 0;
      }
      return -1;
    } else return 1;
  }

}
