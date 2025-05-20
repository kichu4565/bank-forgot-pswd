// src/app/components/forgot-password/forgot-password.component.ts

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-forgot-password',
  standalone:false,
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent {
  forgotForm: FormGroup;
  token: string = '';
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService, private accountService: AccountService) {
    this.forgotForm = this.fb.group({
      accNo: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]{10}$/)]],
      newPass: ['', [Validators.required, Validators.minLength(8),Validators.pattern(/^(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$/)]],
      confirmPass: ['', [Validators.required, Validators.minLength(8)]],
      enteredToken: ['']
    });
  }
  tokenGenerated: boolean = false;

goToLogin() {
  window.location.href = '/login'; 
}


  get accNo() {
    return this.forgotForm.get('accNo');
  }

  get newPass() {
    return this.forgotForm.get('newPass');
  }

  get confirmPass() {
    return this.forgotForm.get('confirmPass');
  }

  get enteredToken() {
    return this.forgotForm.get('enteredToken');
  }

  generateToken() {
    this.errorMessage = '';
    this.successMessage = '';
    const accountNumber = this.accNo?.value.trim();
    console.log('Checking account number:', accountNumber);
    // Check if account exists before proceeding
    this.accountService.getAccountDetails(accountNumber).subscribe({
      next: (details) => {
        // Account exists, proceed to generate token (your existing logic here)
        this.token = Math.random().toString(36).toUpperCase().replace(".","");
        this.tokenGenerated = true;
        this.errorMessage = '';
        this.successMessage = `Token generated: ${this.token}`;
      },
      error: (err) => {
        console.error('Account details error:', err);
        this.errorMessage = err.error?.message || 'Account number not found. Please check and try again.';
      }
    });
  }
  

  resetPassword() {
    this.errorMessage = '';
    this.successMessage = '';

    const enteredToken = this.enteredToken?.value;
    const newPassword = this.newPass?.value;
    const confirmPassword = this.confirmPass?.value;
    const accountNumber = this.accNo?.value;

    if (enteredToken !== this.token) {
      this.errorMessage = 'Invalid token.';
      return;
    }

    if (newPassword !== confirmPassword) {
      this.errorMessage = 'Passwords do not match.';
      return;
    }

    this.authService.resetPassword(accountNumber, newPassword).subscribe({
  next: (res) => {
    this.successMessage = res.message || 'Password reset successfully!';
  },
  error: (err) => {
    console.error('Reset error:', err);
    this.errorMessage = err.error?.message || 'Failed to reset password.';
  }
});

  }
}
