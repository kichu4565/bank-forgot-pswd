import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Transaction {
  id: number;
  accountNumber: string;
  transactionType: 'CREDIT' | 'DEBIT';
  amount: number;
  transactionDate: string;
  description: string;
  balance: number;
}

export interface TransferRequest {
  destinationAccountNumber: string;
  amount: number;
  description: string;
}

export interface TransferResponse {
  success: boolean;
  message: string;
  transactionId: number;
}

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/transactions`;

  constructor(private http: HttpClient) {}

  getRecentTransactions(accountNumber: string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/recent/${accountNumber}`);
  }

  getTransactions(accountNumber: string, days: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/history/${accountNumber}?days=${days}`);
  }

  transfer(transferData: TransferRequest): Observable<TransferResponse> {
    return this.http.post<TransferResponse>(`${this.apiUrl}/transfer`, transferData);
  }
} 