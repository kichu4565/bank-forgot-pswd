import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
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

  constructor(private fb: FormBuilder, private router: Router, private apiService: ApiService) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      fullName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      dateOfBirth: ['', Validators.required],
      nationality: ['India', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      permanentAddress: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(200)]],
      governmentIssuedID: ['', Validators.required],
      idNumber: ['', [Validators.required, Validators.minLength(6)]],
      accountType: ['SAVINGS', Validators.required],
      initialDeposit: [500, [Validators.required, Validators.min(500)]]
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
}
