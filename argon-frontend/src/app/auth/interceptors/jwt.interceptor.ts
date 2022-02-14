import { environment } from '../../../environments/environment';
import { Injectable } from '@angular/core';
import {
  HttpInterceptor, HttpHandler, HttpRequest
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class JWTInterceptor implements HttpInterceptor {
  private static URL_EXCEPTIONS = [
    environment.apiUrl + 'login',
    environment.apiUrl + 'register'
  ]

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (JWTInterceptor.URL_EXCEPTIONS.some((exceptionUrl) => exceptionUrl === req.url)) {
      return next.handle(req);
    }
    const token = this.authService.getToken();
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(req);
  }
}
