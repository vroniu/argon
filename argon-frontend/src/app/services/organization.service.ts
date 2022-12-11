import { Organization } from './../models/organization.model';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../environments/environment';
import { Injectable } from '@angular/core';
import { Employee } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {
  private static ORGANIZATION_URL = environment.apiUrl + 'organizations';

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

  createOrganization(organization: Organization): Observable<Organization> {
    return this.http.post<Organization>(OrganizationService.ORGANIZATION_URL, organization);
  }

  getOrganizationEmployeeInfo(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${OrganizationService.ORGANIZATION_URL}/${id}/employees/me`);
  }

}
