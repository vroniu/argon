import { Observable } from 'rxjs';
import { Subproject } from './../models/subproject.model';
import { Project } from 'src/app/models/project.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private static PROJECT_URL = environment.apiUrl + 'projects'
  private static SUBPROJECT_URL = environment.apiUrl + 'subprojects'

  constructor(private http: HttpClient) { }

  getProjectsForOrganization(organizationId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${ProjectService.PROJECT_URL}/organization/${organizationId}`);
  }

  createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(ProjectService.PROJECT_URL, project);
  }

  updateProject(project: Project): Observable<Project> {
    return this.http.put<Project>(ProjectService.PROJECT_URL, project);
  }

  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${ProjectService.PROJECT_URL}/${id}`)
  }

  createSubproject(subproject: Subproject): Observable<Subproject> {
    return this.http.post<Subproject>(ProjectService.SUBPROJECT_URL, subproject);
  }

  updateSubproject(subproject: Subproject): Observable<Subproject> {
    return this.http.put<Subproject>(ProjectService.SUBPROJECT_URL, subproject);
  }

  deleteSubproject(id: number): Observable<void> {
    return this.http.delete<void>(`${ProjectService.SUBPROJECT_URL}/${id}`)
  }

}
