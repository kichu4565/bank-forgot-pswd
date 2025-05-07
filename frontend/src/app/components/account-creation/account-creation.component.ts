import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountService, CreateAccountRequest, CreateAccountResponse } from '../../services/account.service';

@Component({
  selector: 'app-account-creation',
  templateUrl: './account-creation.component.html',
  styleUrls: ['./account-creation.component.css']
})
export class AccountCreationComponent implements OnInit {
  accountForm: FormGroup;
  loading = false;
  error: string | null = null;
  showPassword = false;
  accountCreated = false;
  createdAccount: any = null;

  constructor(
    private formBuilder: FormBuilder,
    private accountService: AccountService,
    private router: Router
  ) {
    this.accountForm = this.formBuilder.group({
      fullName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      address: ['', [Validators.required, Validators.minLength(10)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      initialDeposit: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    // Check if user is already logged in
    if (localStorage.getItem('accountNumber')) {
      this.router.navigate(['/home']);
    }
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.accountForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const formData: CreateAccountRequest = {
      ...this.accountForm.value,
      initialDeposit: parseFloat(this.accountForm.value.initialDeposit)
    };

    this.accountService.createAccount(formData).subscribe({
      next: (response: CreateAccountResponse) => {
        this.loading = false;
        this.accountCreated = true;
        this.createdAccount = response;
      },
      error: (error: any) => {
        this.loading = false;
        this.error = error.error?.message || 'An error occurred while creating your account. Please try again.';
      }
    });
  }

  goToLogin(): void {
    this.router.navigate(['/login']);
  }
} 