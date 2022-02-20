import { RegistrationRequest } from './../models/register.model';
import { environment } from '../../../environments/environment';
import { LoginRequest } from '../models/login.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, mapTo, tap } from 'rxjs/operators';
import { User } from 'src/app/models/user.model';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private static LOGIN_URL = environment.apiUrl + 'login';
  private static REGISTER_URL = environment.apiUrl + 'register';
  private static USER_INFO_URL = environment.apiUrl + 'user';

  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('user')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  login(login: LoginRequest): Observable<any> {
    return this.http.post<any>(AuthService.LOGIN_URL, login)
    .pipe(
      tap(token => {
        this.storeToken(token.jwt);
        this.http.get<any>(AuthService.USER_INFO_URL).subscribe((userInfo) => {
          localStorage.setItem('user', JSON.stringify(userInfo));
          this.currentUserSubject.next(userInfo);
        });
      }),
      mapTo(true),
      catchError(error => {
        throw error;
      })
    );
  }

  register(register: RegistrationRequest): Observable<any> {
    return this.http.post<any>(AuthService.REGISTER_URL, register);
  }

  storeToken(jwt: string) {
    localStorage.setItem('jwt', jwt);
  }

  getToken(): string {
    return localStorage.getItem('jwt');
  }

  deleteToken() {
    localStorage.removeItem('jwt');
  }

  getCurrentUser(): User {
    return this.currentUserSubject.value;
  }

  logOut() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
  }
}
