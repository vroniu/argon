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
  private URL_EXCEPTIONS: string[] = [];

  constructor(private authService: AuthService) {
    this.URL_EXCEPTIONS = environment.jwtExceptionUrls;
  }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (this.URL_EXCEPTIONS.some((exceptionUrl) => exceptionUrl === req.url)) {
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
