import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-statement',
  standalone: false,
  templateUrl: './statement.component.html',
  styleUrl: './statement.component.css'
})
export class StatementComponent {
  statementForm: FormGroup;
  isLoading = false;
  error = '';
  today = new Date().toISOString().split('T')[0];
  accountNumber = '';
  transactions: any[] = [];

  constructor(private fb: FormBuilder, private apiService: ApiService) {
    this.accountNumber = localStorage.getItem('accountNumber') || '';
    this.statementForm = this.fb.group({
      fromDate: ['', Validators.required],
      toDate: ['', Validators.required]
    });
  }

  downloadStatement() {
    this.error = '';
    if (this.statementForm.valid) {
      this.isLoading = true;
      const fromDate = this.statementForm.get('fromDate')?.value;
      const toDate = this.statementForm.get('toDate')?.value;
      this.apiService.downloadStatement(this.accountNumber, fromDate, toDate).subscribe({
        next: (blob: Blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `statement_${this.accountNumber}_${fromDate}_to_${toDate}.pdf`;
          a.click();
          window.URL.revokeObjectURL(url);
          this.isLoading = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to download statement.';
          this.isLoading = false;
        }
      });
    }
  }

  getTransactionsForRange() {
    this.error = '';
    if (this.statementForm.valid) {
      this.isLoading = true;
      const fromDate = this.statementForm.get('fromDate')?.value;
      const toDate = this.statementForm.get('toDate')?.value;
      this.apiService.getTransactionHistoryForRange(this.accountNumber, fromDate, toDate).subscribe({
        next: (data: any[]) => {
          this.transactions = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to fetch transactions.';
          this.isLoading = false;
        }
      });
    }
  }
}
