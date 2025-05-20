import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface LoginRequest {
  accountNumber: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  token?: string;
  refreshToken?: string;
  refreshTokenExpiry?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient) {}

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return new Observable(observer => {
      this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).subscribe({
        next: (response) => {
          if (response.token) {
            localStorage.setItem('token', response.token);
            localStorage.setItem('accountNumber', credentials.accountNumber);
          }
          if (response.refreshToken) {
            localStorage.setItem('refreshToken', response.refreshToken);
            if (response.refreshTokenExpiry) {
              localStorage.setItem('refreshTokenExpiry', response.refreshTokenExpiry);
            }
          }
          observer.next(response);
          observer.complete();
        },
        error: (err) => {
          observer.error(err);
        }
      });
    });
  }

  resetPassword(accountNumber: string, newPassword: string): Observable<{ message: string }> {
  return this.http.post<{ message: string }>(`${this.apiUrl}/reset-password`, { accountNumber, newPassword });
}


  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  getAccountNumber(): string | null {
    return localStorage.getItem('accountNumber');
  }

  refreshToken(): Observable<any> {
    const refreshToken = this.getRefreshToken();
    const accountNumber = this.getAccountNumber();
    return this.http.post<any>(`${this.apiUrl}/refresh-token`, {
      refreshToken,
      accountNumber
    });
  }

  logout(): void {
    localStorage.removeItem('accountNumber');
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('refreshTokenExpiry');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
