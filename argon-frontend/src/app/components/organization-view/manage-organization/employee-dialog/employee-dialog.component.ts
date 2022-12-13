import { Employee } from './../../../../models/employee.model';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import * as _ from 'lodash';

@Component({
  selector: 'arg-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.css']
})
export class EmployeeDialogComponent implements OnInit {
  @Input()
  employee: Employee;

  constructor(private dialogRef: MatDialogRef<EmployeeDialogComponent>, @Inject(MAT_DIALOG_DATA) private data: Employee) { }

  ngOnInit(): void {
    this.employee = _.cloneDeep(this.data);
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

}
