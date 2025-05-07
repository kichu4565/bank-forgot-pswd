import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }

  // Account related endpoints
  createAccount(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/accounts/create`, data);
  }

  getAccountDetails(accountId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/${accountId}`);
  }

  // Transaction related endpoints
  transferMoney(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/transactions`, data);
  }

  getTransactionHistory(accountId: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/transactions/history/${accountId}`);
  }

  // Authentication endpoints
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/login`, credentials);
  }

  // register(userData: any): Observable<any> {
  //   return this.http.post(`${this.apiUrl}/auth/register`, userData);
  // }

  getAccountBalance(accountNumber: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/accounts/${accountNumber}/balance`);
  }

  createFixedDeposit(data: any) {
    return this.http.post(`${this.apiUrl}/fixed-deposits`, data);
  }

  getFixedDeposits(accountNumber: string) {
    return this.http.get(`${this.apiUrl}/fixed-deposits/account/${accountNumber}`);
  }

  downloadStatement(accountNumber: string, fromDate: string, toDate: string) {
    return this.http.post(
      `${this.apiUrl}/statements/${accountNumber}/download`,
      { fromDate, toDate },
      { responseType: 'blob' }
    );
  }
} 