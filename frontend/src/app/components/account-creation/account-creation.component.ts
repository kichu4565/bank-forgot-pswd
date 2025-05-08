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

  public governmentIdTypes = [
    { label: 'Aadhaar Card', value: 'aadhaar', placeholder: 'Enter your Aadhaar Card Number', pattern: '^[0-9]{12}$', error: 'Aadhaar number must be 12 digits.' },
    { label: 'PAN Card', value: 'pan', placeholder: 'Enter your PAN Card Number', pattern: '^[A-Z]{5}[0-9]{4}[A-Z]{1}$', error: 'PAN must be 10 characters (e.g., ABCDE1234F).' },
    { label: 'Passport', value: 'passport', placeholder: 'Enter your Passport Number', pattern: '^[A-PR-WYa-pr-wy][1-9][0-9]{6}$', error: 'Passport must be 8 characters (e.g., A1234567).' }
  ];
  public selectedIdType = this.governmentIdTypes[0];

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
      initialDeposit: ['', [Validators.required, Validators.min(0)]],
      governmentIssuedID: [this.governmentIdTypes[0].value, Validators.required],
      idNumber: ['', [Validators.required, Validators.pattern(this.governmentIdTypes[0].pattern)]],
      accountType: ['Saving', Validators.required]
    });

    this.accountForm.get('governmentIssuedID')?.valueChanges.subscribe((type) => {
      const idType = this.governmentIdTypes.find(t => t.value === type) || this.governmentIdTypes[0];
      this.selectedIdType = idType;
      this.accountForm.get('idNumber')?.setValidators([Validators.required, Validators.pattern(idType.pattern)]);
      this.accountForm.get('idNumber')?.updateValueAndValidity();
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