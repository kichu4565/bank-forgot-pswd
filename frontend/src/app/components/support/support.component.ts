import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-support',
  standalone: false,
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  supportForm: FormGroup;
  isLoading = false;
  success: string | null = null;
  error: string | null = null;

  constructor(private fb: FormBuilder) {
    this.supportForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      message: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.supportForm.invalid) return;
    this.isLoading = true;
    this.success = null;
    this.error = null;
    // Simulate sending message
    setTimeout(() => {
      this.isLoading = false;
      this.success = 'Your message has been sent! Our team will contact you soon.';
      this.supportForm.reset();
    }, 1200);
  }
}
