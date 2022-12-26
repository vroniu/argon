import { Employee } from './../models/employee.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private static EMPLOYEE_URL = environment.apiUrl + 'employees';

  constructor(private http: HttpClient) { }

  getEmployees(excludeOrganizations?: number[], name?: string, email?: string): Observable<Employee[]> {
    let params = new HttpParams();
    if (name) {
      params = params.set('name', name);
    }
    if (email) {
      params = params.set('email', email);
    }
    if (excludeOrganizations) {
      params = params.set('excludeOrganizations', JSON.stringify(excludeOrganizations));
    }
    return this.http.get<Employee[]>(EmployeeService.EMPLOYEE_URL, { params: params });
  }
}
