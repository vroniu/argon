import { TestUtils } from './../../../tests/test-utils';
import { environment } from '../../../environments/environment';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HTTP_INTERCEPTORS } from "@angular/common/http";
import { TestBed } from '@angular/core/testing';
import { ErrorInterceptor } from './error.interceptor';

describe('ErrorInterceptor', () => {
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;
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
    errorInterceptor = TestUtils.getInterceptorInstance<ErrorInterceptor>(TestBed.inject(HTTP_INTERCEPTORS), ErrorInterceptor);
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should not log out user if authorized', () => {
    const interceptorSpy = spyOn(errorInterceptor, 'intercept').and.callThrough();
    const reloadSpy = spyOn(errorInterceptor, 'reloadPage').and.callFake(() => {});
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe((response) => {
      expect(interceptorSpy).toHaveBeenCalled();
      expect(reloadSpy).not.toHaveBeenCalled();
      expect(response).toBeTruthy();
    });

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    request.flush({data: true});

    httpTestingController.verify();
  });

  // TODO fix this mf
  it('should log out user if unauthorized', () => {
    const interceptorSpy = spyOn(errorInterceptor, 'intercept').and.callThrough();
    const reloadSpy = spyOn(errorInterceptor, 'reloadPage').and.callFake(() => {});
    httpClient.get<any>(environment.apiUrl + 'endpoint').subscribe(
      () => { },
      () => {
        expect(interceptorSpy).toHaveBeenCalled();
        expect(reloadSpy).toHaveBeenCalled();
      }
    );

    const request = httpTestingController.expectOne(environment.apiUrl + 'endpoint');
    request.flush({data: true}, { status: 401, statusText: 'Unauthorized' });

    httpTestingController.verify();
  });
});
