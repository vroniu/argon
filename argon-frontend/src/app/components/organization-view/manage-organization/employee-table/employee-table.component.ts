import { AuthService } from 'src/app/auth/services/auth.service';
import { Employee } from './../../../../models/employee.model';
import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { Worktime } from 'src/app/models/worktime.model';

@Component({
  selector: 'arg-employee-table',
  templateUrl: './employee-table.component.html',
  styleUrls: ['./employee-table.component.css']
})

export class EmployeeTableComponent implements OnInit {
  @Input() employees: Employee[];
  @Output() onEmployeeEdit = new EventEmitter<Employee>();
  @Output() onEmployeeDelete = new EventEmitter<Employee>();
  currentUserId: number;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUserId = this.authService.getCurrentUser().id;
  }

  deleteWorktime(worktime: Employee) {
    this.onEmployeeDelete.emit(worktime);
  }

  editWorktime(worktime: Employee) {
    this.onEmployeeEdit.emit(worktime);
  }

}
