import { AuthService } from './../../../../auth/services/auth.service';
import { Organization } from './../../../../models/organization.model';
import { OrganizationService } from './../../../../services/organization.service';
import { Employee } from './../../../../models/employee.model';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as _ from 'lodash';

export interface EmployeeDialogData {
  employee: Employee,
  organization: Organization
}

@Component({
  selector: 'arg-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.css']
})
export class EmployeeDialogComponent implements OnInit {
  employee: Employee;
  organization: Organization;
  employeeIsOwner: boolean;
  employeeIsUser: boolean;

  constructor(private dialogRef: MatDialogRef<EmployeeDialogComponent>, @Inject(MAT_DIALOG_DATA) private data: EmployeeDialogData,
    private organizationService: OrganizationService, private authService: AuthService) { }

  ngOnInit(): void {
    this.employee = _.cloneDeep(this.data.employee);
    this.organization = _.cloneDeep(this.data.organization);
    this.setEmployeeIsOwner();
    this.setEmployeeIsUser();
  }

  promoteEmployeeToOwner() {
    this.organizationService.promoteEmployeeToOwner(this.data.organization.id, this.employee).subscribe(() => {
      this.organizationService.getOrganizationById(this.organization.id).subscribe((organization) => {
        this.organization = organization;
        this.setEmployeeIsOwner();
      })
    });
  }

  demoteEmployeeFromOwner() {
    this.organizationService.demoteEmployeeFromOwner(this.data.organization.id, this.employee).subscribe(() => {
      this.organizationService.getOrganizationById(this.organization.id).subscribe((organization) => {
        this.organization = organization;
        this.setEmployeeIsOwner();
      })
    });
  }

  onClose(save: boolean) {
    if (save) {
      this.dialogRef.close({
        save: true,
        employee: this.employee
      });
    } else {
      this.dialogRef.close({ save: false });
    }
  }

  private setEmployeeIsOwner() {
    this.employeeIsOwner = this.organization.owners.some(owner => owner.id === this.employee.id)
  }

  private setEmployeeIsUser() {
    this.employeeIsUser = this.authService.getCurrentUser().id == this.employee.id
  }

}
