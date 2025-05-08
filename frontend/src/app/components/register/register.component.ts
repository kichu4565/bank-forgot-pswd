import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  created = false;
  userData: any;
  today: string = new Date().toISOString().split('T')[0];
  backendResponse: any;

  maxDate: Date = new Date(new Date().setFullYear(new Date().getFullYear() - 15));

  idNumberPlaceholder = 'Enter your Aadhaar Card Number';
  idNumberError = 'Aadhaar number must be 12 digits.';

  private idTypes = {
    aadhaar: {
      placeholder: 'Enter your Aadhaar Card Number',
      pattern: /^[0-9]{12}$/,
      error: 'Aadhaar number must be 12 digits.'
    },
    pan: {
      placeholder: 'Enter your PAN Card Number',
      pattern: /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/,
      error: 'PAN must be 10 characters (e.g., ABCDE1234F).'
    },
    passport: {
      placeholder: 'Enter your Passport Number',
      pattern: /^[A-PR-WYa-pr-wy][1-9][0-9]{6}$/,
      error: 'Passport must be 8 characters (e.g., A1234567).'
    }
  };

  constructor(private fb: FormBuilder, private router: Router, private apiService: ApiService) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      dateOfBirth: ['', [Validators.required, this.minAgeValidator(15)]],
      nationality: ['India', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      permanentAddress: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      governmentIssuedID: ['aadhaar', Validators.required],
      idNumber: ['', [Validators.required, Validators.pattern(this.idTypes.aadhaar.pattern)]],
      accountType: ['SAVINGS', Validators.required],
      initialDeposit: [500, [Validators.required, Validators.min(500)]]
    });

    this.registerForm.get('governmentIssuedID')?.valueChanges.subscribe(type => {
      this.onIdTypeChange({ target: { value: type } });
    });
  }

  get fullName() { return this.registerForm.get('fullName'); }
  get dateOfBirth() { return this.registerForm.get('dateOfBirth'); }
  get nationality() { return this.registerForm.get('nationality'); }
  get phoneNumber() { return this.registerForm.get('phoneNumber'); }
  get permanentAddress() { return this.registerForm.get('permanentAddress'); }
  get governmentIssuedID() { return this.registerForm.get('governmentIssuedID'); }
  get idNumber() { return this.registerForm.get('idNumber'); }
  get accountType() { return this.registerForm.get('accountType'); }
  get initialDeposit() { return this.registerForm.get('initialDeposit'); }

  onIdTypeChange(event: any) {
    const type = event.target.value as 'aadhaar' | 'pan' | 'passport';
    const idType = this.idTypes[type] || this.idTypes.aadhaar;
    this.idNumberPlaceholder = idType.placeholder;
    this.idNumberError = idType.error;
    this.registerForm.get('idNumber')?.setValidators([Validators.required, Validators.pattern(idType.pattern)]);
    this.registerForm.get('idNumber')?.updateValueAndValidity();
  }

  onCreate() {
    if (this.registerForm.valid) {
      this.apiService.createAccount(this.registerForm.value).subscribe({
        next: (res) => {
          this.backendResponse = res;
          this.created = true;
        },
        error: (err) => {
          alert('Account creation failed: ' + (err.error?.message || err.message));
        }
      });
    }
  }

  goToLogin() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  minAgeValidator(minAge: number) {
    return (control: AbstractControl) => {
      const value = control.value;
      if (!value) return null;
      const birthDate = new Date(value);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const m = today.getMonth() - birthDate.getMonth();
      if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      return age >= minAge ? null : { minAge: true };
    };
  }
}
