import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CreateAccountRequest {
  fullName: string;
  email: string;
  phone: string;
  address: string;
  password: string;
  initialDeposit: number;
}

export interface CreateAccountResponse {
  accountNumber: string;
  message: string;
}

export interface AccountDetails {
  accountNumber: string;
  accountHolderName: string;
  phoneNumber: string;
  permanentAddress: string;
  governmentIssuedID: string;
  idNumber: string;
  accountType: string;
  balance: number;
  accountCreationDate: string;
}

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = `${environment.apiUrl}/accounts`;

  constructor(private http: HttpClient) {}

  createAccount(data: CreateAccountRequest): Observable<CreateAccountResponse> {
    return this.http.post<CreateAccountResponse>(`${this.apiUrl}/create`, data);
  }

  getAccountDetails(accountNumber: string): Observable<AccountDetails> {
    return this.http.get<AccountDetails>(`${this.apiUrl}/${accountNumber}`);
  }

  updateAccount(accountNumber: string, data: Partial<AccountDetails>): Observable<AccountDetails> {
    return this.http.put<AccountDetails>(`${this.apiUrl}/${accountNumber}`, data);
  }
} 