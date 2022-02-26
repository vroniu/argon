import { DateTime } from 'luxon';
import { Observable, Subject } from 'rxjs';
import { environment } from './../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Worktime } from '../models/worktime.model';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class WorktimeService {
  private formatWorktimeDay = (worktime: Worktime) => {
    worktime.day = new Date(DateTime.fromJSDate(worktime.day).toFormat('yyyy-MM-dd'));
  }
  private static WORKTIME_URL = environment.apiUrl + 'worktimes';
  private worktimesUpdated = new Subject<Date>();

  constructor(private http: HttpClient) { }

  getWorktimesAtDay(day: Date, organizationId: number): Observable<Worktime[]> {
    return this.http.get<Worktime[]>(WorktimeService.WORKTIME_URL + '/day', {
      params: new HttpParams()
        .set('day', DateTime.fromJSDate(day).toFormat('yyyy-MM-dd'))
        .set('organizationId', organizationId)
    });
  }

  getWorktimesAtDateRange(rangeStart: Date, rangeEnd: Date, organizationId: number): Observable<Worktime[]> {
    return this.http.get<Worktime[]>(WorktimeService.WORKTIME_URL + '/range', {
      params: new HttpParams()
        .set('rangeStart', DateTime.fromJSDate(rangeStart).toFormat('yyyy-MM-dd'))
        .set('rangeEnd', DateTime.fromJSDate(rangeEnd).toFormat('yyyy-MM-dd'))
        .set('organizationId', organizationId)
    });
  }

  createWorktime(worktime: Worktime): Observable<Worktime> {
    this.formatWorktimeDay(worktime);
    return this.http.post<Worktime>(WorktimeService.WORKTIME_URL, worktime)
      .pipe(tap((worktime) => this.worktimesUpdated.next(worktime.day)));
  }

  updateWorktime(worktime: Worktime): Observable<Worktime> {
    this.formatWorktimeDay(worktime);
    return this.http.put<Worktime>(WorktimeService.WORKTIME_URL, worktime)
      .pipe(tap((worktime) => this.worktimesUpdated.next(worktime.day)))
  }

  deleteWorktime(worktime: Worktime): Observable<void> {
    return this.http.delete<void>(`${WorktimeService.WORKTIME_URL}/${worktime.id}`)
      .pipe(tap(() => this.worktimesUpdated.next(worktime.day)))
  }

  getWorktimesUpdated(): Observable<Date> {
    return this.worktimesUpdated.asObservable();
  }
}
