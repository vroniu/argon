import { Organization } from './../models/organization.model';
import { Observable, Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { Employee } from '../models/employee.model';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  private static ORGANIZATION_URL = environment.apiUrl + 'organizations';
  private organizationsUpdated = new Subject<number>();

  constructor(private http: HttpClient) { }

  getOrganizationsOwnedByUser(): Observable<Organization[]> {
    return this.http.get<Organization[]>(OrganizationService.ORGANIZATION_URL + '/owned');
  }

  getOrganizationsJoinedByUser(): Observable<Organization[]> {
    return this.http.get<Organization[]>(OrganizationService.ORGANIZATION_URL + '/joined');
  }

  getOrganizationById(id: number): Observable<Organization> {
    return this.http.get<Organization>(`${OrganizationService.ORGANIZATION_URL}/${id}`);
  }

  getOrganizationEmployees(id: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees`);
  }

  updateEmployeeInfoForOrganization(id: number, employee: Employee): Observable<Employee> {
    return this.http.put<Employee>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees`, employee);
  }

  createOrganization(organization: Organization): Observable<Organization> {
    return this.http.post<Organization>(OrganizationService.ORGANIZATION_URL, organization);
  }

  addEmployeeToOrganization(id: number, employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees`, employee)
      .pipe(tap(() => this.organizationsUpdated.next(id)));
  }

  updateOrganization(organization: Organization): Observable<Organization> {
    return this.http.put<Organization>(OrganizationService.ORGANIZATION_URL, organization)
      .pipe(tap(() => this.organizationsUpdated.next(organization.id)));
  }

  getOrganizationEmployeeInfo(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees/me`);
  }

  deleteEmployeeFromOrganization(id: number, employeeId: number): Observable<void> {
    return this.http.delete<void>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees/${employeeId}`);
  }

  getOrganizationsUpdated(): Observable<number> {
    return this.organizationsUpdated;
  }

}
