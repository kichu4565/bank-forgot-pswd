import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';
import { AccountService, AccountDetails } from '../../services/account.service';
import { TransactionService, Transaction } from '../../services/transaction.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  showBalance = false;
  accountDetails: AccountDetails | null = null;
  balance: number | null = null;
  accountNumber: string = '';
  transactions: Transaction[] = [];
  loading: boolean = true;
  error: string | null = null;
  displayedBalance: number = 0;
  animationFrame: number | null = null;

  constructor(
    private router: Router,
    private apiService: ApiService,
    private accountService: AccountService,
    private transactionService: TransactionService
  ) {}

  ngOnInit(): void {
    this.accountNumber = localStorage.getItem('accountNumber') || '';
    if (this.accountNumber) {
      this.loadAccountDetails(this.accountNumber);
      this.loadRecentTransactions(this.accountNumber);
    } else {
      this.router.navigate(['/login']);
    }
  }

  private loadAccountDetails(accountNumber: string): void {
    this.accountService.getAccountDetails(accountNumber).subscribe({
      next: (data: AccountDetails) => {
        this.accountDetails = data;
        this.loading = false;
        const balance = Number(data.balance);
        console.log('Animating balance to:', balance);
        this.animateBalance(balance);
      },
      error: (error: any) => {
        this.error = 'Failed to load account details';
        this.loading = false;
        console.error('Error loading account details:', error);
      }
    });
  }

  private loadRecentTransactions(accountNumber: string): void {
    this.transactionService.getRecentTransactions(accountNumber).subscribe({
      next: (data: Transaction[]) => {
        this.transactions = data.slice(0, 5); // Get only the 5 most recent transactions
      },
      error: (error: any) => {
        console.error('Error loading recent transactions:', error);
      }
    });
  }

  toggleBalance() {
    if (!this.showBalance && this.accountNumber) {
      this.apiService.getAccountBalance(this.accountNumber).subscribe({
        next: (data) => {
          this.balance = data.balance || data;
          this.showBalance = true;
        },
        error: (err) => {
          this.balance = null;
          this.showBalance = true;
        }
      });
    } else {
      this.showBalance = false;
    }
  }

  logout() {
    const confirmed = confirm('Do you want to exit?');
    if (confirmed) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }

  private animateBalance(target: number) {
    if (this.animationFrame) {
      cancelAnimationFrame(this.animationFrame);
    }
    let start = 0;
    const duration = 900;
    const startTime = performance.now();
    const animate = (now: number) => {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      this.displayedBalance = start + (target - start) * progress;
      if (progress < 1) {
        this.animationFrame = requestAnimationFrame(animate);
      } else {
        this.displayedBalance = target;
      }
    };
    this.animationFrame = requestAnimationFrame(animate);
  }
}