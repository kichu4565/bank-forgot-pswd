import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { TransactionService, Transaction } from '../../services/transaction.service';
import { AccountService } from '../../services/account.service';
// import { ChartOptions, ChartType, ChartData } from 'chart.js';

@Component({
  selector: 'app-transaction',
  standalone: false,
  templateUrl: './transaction.component.html',
  styleUrl: './transaction.component.css'
})
export class TransactionComponent implements OnInit {
  transferForm: FormGroup;
  transactions: Transaction[] = [];
  loading: boolean = false;
  error: string | null = null;
  success: string | null = null;
  public accountNumber: string = '';
  recipientName: string | null = null;
  showConfirmation: boolean = false;
  confirmationDetails: any = null;
  accountNotRegistered: boolean = false;

  constructor(
    private fb: FormBuilder,
    private transactionService: TransactionService,
    private router: Router,
    private accountService: AccountService,
    
  ) {
    this.transferForm = this.fb.group({
recipientAccount: ['', [Validators.required, Validators.pattern('^BANK[A-Z0-9]{6}$')]],
      amount: ['', [Validators.required, Validators.min(1)]],
      description: ['', Validators.required]
    });

    this.transferForm.get('recipientAccount')?.valueChanges.subscribe(accountNumber => {
      this.recipientName = null;
      this.accountNotRegistered = false;
      if (this.transferForm.get('recipientAccount')?.valid) {
        this.accountService.getAccountDetails(accountNumber).subscribe({
          next: (details) => {
            this.recipientName = details.accountHolderName;
            this.accountNotRegistered = false;
          },
          error: () => {
            this.recipientName = null;
            this.accountNotRegistered = true;
          }
        });
      } else {
        this.accountNotRegistered = false;
      }
    });
  }

  ngOnInit(): void {
    this.accountNumber = localStorage.getItem('accountNumber') || '';
    this.loadTransactions();
  }

  onSubmit(): void {
    if (this.transferForm.valid) {
      this.loading = true;
      this.error = null;
      this.success = null;
      this.showConfirmation = false;
      this.confirmationDetails = null;

      const accountNumber = localStorage.getItem('accountNumber');
      if (!accountNumber) {
        this.router.navigate(['/login']);
        return;
      }

      const transferData = {
        destinationAccountNumber: this.transferForm.value.recipientAccount,
        amount: this.transferForm.value.amount,
        description: this.transferForm.value.description
      };

      this.transactionService.transfer(transferData).subscribe({
        next: (response) => {
          this.success = 'Transfer successful!';
          this.confirmationDetails = {
            recipientAccount: this.transferForm.value.recipientAccount,
            recipientName: this.recipientName,
            amount: this.transferForm.value.amount,
            description: this.transferForm.value.description,
            transactionId: response.transactionId
          };
          this.showConfirmation = true;
          this.transferForm.reset();
          this.recipientName = null;
          this.loadTransactions();
        },
        error: (error) => {
          if (error.status === 404) {
            this.error = 'Recipient account not found. Please check the account number.';
          } else if (error.status === 400) {
            this.error = error.error?.message || 'Invalid transfer request. Please check your input.';
          } else if (error.status === 500) {
            this.error = error.error?.message || 'An error occurred while processing your transfer. Please try again.';
          } else {
            this.error = 'Transfer failed. Please try again.';
          }
          this.loading = false;
        },
        complete: () => {
          this.loading = false;
        }
      });
    }
  }

  onDateRangeChange(event: Event): void {
    const days = (event.target as HTMLSelectElement).value;
    this.loadTransactions(parseInt(days));
  }

  private loadTransactions(days: number = 7): void {
    const accountNumber = localStorage.getItem('accountNumber');
    if (!accountNumber) {
      this.router.navigate(['/login']);
      return;
    }

    this.transactionService.getTransactions(accountNumber, days).subscribe({
      next: (data) => {
        this.transactions = data;
      },
      error: (error) => {
        this.error = 'Failed to load transactions';
        console.error('Error loading transactions:', error);
      }
    });
  }
   logout() {
    let confirmed = confirm('Do you want to exit?');
    if (confirmed) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}

