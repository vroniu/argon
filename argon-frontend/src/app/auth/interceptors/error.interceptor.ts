import { AuthService } from './../services/auth.service';
import { tap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return next.handle(req).pipe(tap(
      () => {},
      (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            // TODO - use toastService to notify why user has been loged out
          } else {
            return;
          }
        }
      }
      ))
    }

  reloadPage(): void {
    this.authService.logOut();
    window.location.reload();
  }
}

