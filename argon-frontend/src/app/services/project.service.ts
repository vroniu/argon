import { Observable, Subject } from 'rxjs';
import { Subproject } from './../models/subproject.model';
import { Project } from 'src/app/models/project.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private static PROJECT_URL = environment.apiUrl + 'projects'
  private static SUBPROJECT_URL = environment.apiUrl + 'subprojects'

  private projectsUpdated = new Subject<number>();

  constructor(private http: HttpClient) { }

  getProjectsForOrganization(organizationId: number): Observable<Project[]> {
    return this.http.get<Project[]>(`${ProjectService.PROJECT_URL}/organization/${organizationId}`);
  }

  createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(ProjectService.PROJECT_URL, project)
      .pipe(tap(project => this.projectsUpdated.next(project.organizationId)));
  }

  updateProject(project: Project): Observable<Project> {
    return this.http.put<Project>(ProjectService.PROJECT_URL, project)
      .pipe(tap(project => this.projectsUpdated.next(project.organizationId)));
  }

  deleteProject(project: Project): Observable<void> {
    return this.http.delete<void>(`${ProjectService.PROJECT_URL}/${project.id}`)
      .pipe(tap(() => this.projectsUpdated.next(project.organizationId)));
  }

  createSubproject(subproject: Subproject): Observable<Subproject> {
    return this.http.post<Subproject>(ProjectService.SUBPROJECT_URL, subproject)
    .pipe(tap(subproject => {
      this.projectsUpdated.next(subproject.organizationId)
    }));
  }

  updateSubproject(subproject: Subproject): Observable<Subproject> {
    return this.http.put<Subproject>(ProjectService.SUBPROJECT_URL, subproject)
      .pipe(tap(subproject => this.projectsUpdated.next(subproject.organizationId)));

  }

  deleteSubproject(subproject: Subproject): Observable<void> {
    return this.http.delete<void>(`${ProjectService.SUBPROJECT_URL}/${subproject.id}`)
      .pipe(tap(() => this.projectsUpdated.next(subproject.organizationId)));
  }

  getProjectsUpdated(): Observable<number> {
    return this.projectsUpdated;
  }

}
