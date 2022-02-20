import { RegistrationRequest } from './../models/register.model';
import { LoginRequest } from './../models/login.model';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { TestBed } from '@angular/core/testing';
import { User } from 'src/app/models/user.model';

import { AuthService } from './auth.service';
import { HttpClient } from '@angular/common/http';
import { of } from 'rxjs';

describe('AuthService', () => {
  let service: AuthService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const testToken = 'jwt.token';
  const testUserData: User = {
    id: 1,
    username: 'testuser',
    email: 'mail@mail.com',
    employee: {
      id: 1,
      firstName: 'John',
      lastName: 'Doe'
    }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    localStorage.clear();
    service = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('storeToken should store token value ', () => {
    service.storeToken(testToken);
    expect(localStorage.getItem('jwt')).toEqual(testToken);
  });

  it('getToken should return stored token', () => {
    localStorage.setItem('jwt', testToken);
    expect(service.getToken()).toEqual(testToken);
  });

  it('deleteToken should delete stored token', () => {
    localStorage.setItem('jwt', testToken);
    service.deleteToken();
    expect(localStorage.getItem('jwt')).toBeFalsy();
  });

  it('login should return true and store the token and user info when loggin in with valid data', () => {
    const testLogin: LoginRequest = {
      username: 'username',
      password: 'password1'
    };
    let userInfoRequestSpy = spyOn(httpClient, 'get').and.returnValue(of(testUserData));

    service.login(testLogin).subscribe((loggedIn) => {
      expect(loggedIn).toBeTruthy();
      expect(service.getToken()).toEqual('jwt.token_from_backend');
      userInfoRequestSpy.calls.mostRecent().returnValue.subscribe(() => {
        expect(service.getCurrentUser()).toEqual(testUserData);
      });
    });

    const loginRequest = httpTestingController.expectOne(environment.apiUrl + 'login');
    expect(loginRequest.request.method).toEqual('POST');
    loginRequest.flush({
      jwt: 'jwt.token_from_backend'
    });

    httpTestingController.verify();
  });

  it('login should throw error when loggin in with invalid data', () => {
    const badCredentialsResponse = {
      status: 'BAD_CREDENTIALS',
      description: 'Incorrect username or password'
    };
    const testLogin: LoginRequest = {
      username: 'invalid_username',
      password: 'invalid_password'
    };

    service.login(testLogin).subscribe(
      () => {},
      (error) => {
        expect(error.error).toEqual(badCredentialsResponse);
      }
    );

    const loginRequest = httpTestingController.expectOne(environment.apiUrl + 'login');
    expect(loginRequest.request.method).toEqual('POST');
    loginRequest.flush(
      badCredentialsResponse,
      {
        status: 400,
        statusText: 'Bad request'
      }
    );

    httpTestingController.verify();
  });

  it('logOut should delete token and user from storage and return null user', () => {
    localStorage.setItem('user', JSON.stringify(testUserData));
    localStorage.setItem('jwt', 'jwt.token');

    service.logOut();

    expect(localStorage.getItem('user')).toBeFalsy();
    expect(localStorage.getItem('jwt')).toBeFalsy();
    expect(service.getCurrentUser()).toBeFalsy();
  });

  it('register should return observable when registering with valid data', () => {
    const testRegister: RegistrationRequest = {
      username: 'username',
      password: 'password1',
      email: 'mail@mail.com',
      employee: {
        firstName: 'John',
        lastName: 'Doe'
      }
    };
    const registerResponse = {
      status: 'SUCCESS',
      description: 'User created successfully'
    };

    service.register(testRegister).subscribe((registered) => {
      expect(registered).toBeTruthy();
    });

    const registerRequest = httpTestingController.expectOne(environment.apiUrl + 'register');
    expect(registerRequest.request.method).toEqual('POST');
    registerRequest.flush(registerResponse);

    httpTestingController.verify();
  });

  it('register should throw error when registering with invalid data', () => {
    const testRegister: RegistrationRequest = {
      username: 'username',
      password: 'password1',
      email: 'mail@mail.com',
      employee: {
        firstName: 'John',
        lastName: 'Doe'
      }
    };
    const registerResponse = {
      status: 'INVALID_REQUEST',
      description: 'The request is invalid'
    };

    service.register(testRegister).subscribe(
      () => {},
      (error) => {
        expect(error.error).toEqual(registerResponse);
      }
    );

    const registerRequest = httpTestingController.expectOne(environment.apiUrl + 'register');
    expect(registerRequest.request.method).toEqual('POST');
    registerRequest.flush(registerResponse, {
      status: 400,
      statusText: 'Bad request'
    });

    httpTestingController.verify();
  });
});
