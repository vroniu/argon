import { environment } from '../../../environments/environment';
import { JWTInterceptor } from './jwt.interceptor';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HTTP_INTERCEPTORS } from "@angular/common/http";
import { AuthService } from '../services/auth.service';
import { TestBed } from '@angular/core/testing';

describe('JwtInterceptor', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        {
          provide: HTTP_INTERCEPTORS,
          useClass: JWTInterceptor,
          multi: true,
        }
      ]
    });
    authService = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should add JWT token when there is one stored', () => {
    spyOn(authService, 'getToken').and.returnValue('jwt.token');
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    expect(request.request.headers.has('Authorization')).toBeTrue();
    expect(request.request.headers.get('Authorization')).toEqual('Bearer jwt.token');
    request.flush({data: true});

    httpTestingController.verify();
  });

  it('shouldnt add JWT token when there is none', () => {
    spyOn(authService, 'getToken').and.returnValue(null);
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe((response) => {
      expect(response).toBeTruthy();
    });

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    expect(request.request.headers.has('Authorization')).toBeFalse()
    expect(request.request.headers.get('Authorization')).toEqual(null);
    request.flush({data: true});

    httpTestingController.verify();
  });

  it('shouldnt add JWT token when url is on the exception list', () => {
    spyOn(authService, 'getToken').and.returnValue('jwt.token');

    environment.jwtExceptionUrls.forEach(exceptionUrl => {
      httpClient.get<any>(exceptionUrl).subscribe((response) => {
        expect(response).toBeTruthy();
      });

      const request = httpTestingController.expectOne(exceptionUrl);
      expect(request.request.headers.has('Authorization')).toBeFalse()
      expect(request.request.headers.get('Authorization')).toEqual(null);
      request.flush({data: true});
    });

    httpTestingController.verify();
  });
})
