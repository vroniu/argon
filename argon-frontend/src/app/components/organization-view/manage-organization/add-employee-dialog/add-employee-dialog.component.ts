import { OrganizationService } from 'src/app/services/organization.service';
import { EmployeeService } from './../../../../services/employee.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Employee } from 'src/app/models/employee.model';

export interface AddEmployeeDialogData {
  organizationId: number;
}

@Component({
  selector: 'arg-add-employee-dialog',
  templateUrl: './add-employee-dialog.component.html',
  styleUrls: ['./add-employee-dialog.component.css']
})
export class AddEmployeeDialogComponent implements OnInit {
  employees: Employee[] = []

  constructor(
    private dialogRef: MatDialogRef<AddEmployeeDialogComponent>,
    private employeeService: EmployeeService,
    private organizationService: OrganizationService,
    @Inject(MAT_DIALOG_DATA) private data: AddEmployeeDialogData
  ) { }

  ngOnInit(): void {
    this.getEmployees();
  }

  addEmployee(employee: Employee): void {
    this.organizationService.addEmployeeToOrganization(this.data.organizationId, employee).subscribe(() => this.getEmployees());
  }

  onClose() {
    this.dialogRef.close();
  }

  private getEmployees(): void {
    this.employeeService.getEmployees([this.data.organizationId]).subscribe(employees => {
      this.employees = employees;
    });
  }

}
