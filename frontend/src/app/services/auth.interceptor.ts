import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { catchError, switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    let cloned = req;
    if (token) {
      cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(cloned).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Try to refresh the token
          return this.authService.refreshToken().pipe(
            switchMap((response: any) => {
              if (response && response.token) {
                localStorage.setItem('token', response.token);
                // Retry the original request with the new token
                const newReq = req.clone({
                  setHeaders: {
                    Authorization: `Bearer ${response.token}`
                  }
                });
                return next.handle(newReq);
              } else {
                this.authService.logout();
                this.router.navigate(['/login']);
                return throwError(() => error);
              }
            }),
            catchError(err => {
              this.authService.logout();
              this.router.navigate(['/login']);
              return throwError(() => err);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
} 