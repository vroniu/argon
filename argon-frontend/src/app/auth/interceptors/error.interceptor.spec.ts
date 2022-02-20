import { AuthService } from './../services/auth.service';
import { environment } from '../../../environments/environment';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HTTP_INTERCEPTORS } from "@angular/common/http";
import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error.interceptor';

describe('ErrorInterceptor', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let authService: AuthService;
  let errorInterceptor: ErrorInterceptor;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: ErrorInterceptor,
          multi: true,
        }
      ]
    });
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
    errorInterceptor = TestBed.inject(ErrorInterceptor);
  });

  it('should not log out user if authorized', () => {
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    request.flush({data: true});

    httpTestingController.verify();
  });

  // TODO fix this mf
  xit('should log out user if unauthorized', () => {
    errorInterceptor.reloadPage = function() {};
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe(
      (response) => { },
      (error) => {
        expect(errorInterceptor.reloadPage.prototype).toHaveBeenCalled();
      }
    );

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    request.flush({data: true}, { status: 401, statusText: 'Unauthorized' });

    httpTestingController.verify();
  });
});
