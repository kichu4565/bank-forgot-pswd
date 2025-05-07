import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-test-connection',
  standalone: false,
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header">
          <h3>API Connection Test</h3>
        </div>
        <div class="card-body">
          <div *ngIf="loading" class="text-center">
            <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>
          <div *ngIf="error" class="alert alert-danger">
            {{ error }}
          </div>
          <div *ngIf="response" class="alert alert-success">
            <pre>{{ response }}</pre>
          </div>
          <button class="btn btn-primary" (click)="testConnection()" [disabled]="loading">
            Test Connection
          </button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .container { max-width: 800px; }
    pre { margin: 0; }
  `]
})
export class TestConnectionComponent implements OnInit {
  loading = false;
  error: string | null = null;
  response: any = null;
  connectionStatus: string | null = null;

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {}

  testConnection(): void {
    this.loading = true;
    this.error = null;
    this.response = null;

    // First create a test account
    const testAccountData = {
      fullName: 'Test User',
      dateOfBirth: new Date('1990-01-01').toISOString().split('T')[0], // Format as YYYY-MM-DD
      nationality: 'Indian',
      phoneNumber: '9876543210',
      permanentAddress: '123 Test Street, Test City',
      governmentIssuedID: 'Aadhaar',
      idNumber: '1234-5678-9101',
      accountType: 'SAVINGS',
      initialDeposit: '1000.00'
    };

    console.log('Sending test account data:', testAccountData);

    this.apiService.createAccount(testAccountData).subscribe({
      next: (response) => {
        console.log('Account created:', response);
        // Now try to login with the created account
        this.apiService.login({
          accountNumber: response.accountNumber,
          password: response.password
        }).subscribe({
          next: (loginResponse) => {
            console.log('Login successful:', loginResponse);
            this.connectionStatus = 'Connection successful!';
            this.loading = false;
          },
          error: (error) => {
            console.error('Login error:', error);
            this.connectionStatus = 'Login failed: ' + error.message;
            this.loading = false;
          }
        });
      },
      error: (error) => {
        console.error('Account creation error:', error);
        this.connectionStatus = 'Account creation failed: ' + error.message;
        this.loading = false;
      }
    });
  }
} 