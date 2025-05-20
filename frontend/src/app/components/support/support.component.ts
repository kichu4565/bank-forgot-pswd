
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';


@Component({
  selector: 'app-support',
  standalone: false,
  templateUrl: './support.component.html',
  styleUrl: './support.component.css',
  // imports: [
  //   CommonModule,
  //   ReactiveFormsModule,
  //   MatIconModule,
  //   MatCardModule,
  //   MatFormFieldModule,
  //   MatSelectModule,
  //   MatInputModule,
  //   MatButtonModule,
  //   MatExpansionModule,
  //   MatSnackBarModule
  // ]
})
export class SupportComponent {
  supportForm: FormGroup;
  loading = false;
  selectedFile: File | null = null;
  showConfirmation = false;
  confirmationDetails: any = null;

  categories = [
    { value: 'general', label: 'General Inquiry', icon: 'help_outline' },
    { value: 'account', label: 'Account Related', icon: 'account_balance' },
    { value: 'transaction', label: 'Transaction Issue', icon: 'payment' },
    { value: 'technical', label: 'Technical Support', icon: 'computer' },
    { value: 'security', label: 'Security Concern', icon: 'security' }
  ];

  priorities = [
    { value: 'low', label: 'Low Priority', icon: 'low_priority', color: '#43b96f' },
    { value: 'medium', label: 'Medium Priority', icon: 'radio_button_unchecked', color: '#2196f3' },
    { value: 'high', label: 'High Priority', icon: 'priority_high', color: '#ff9800' },
    { value: 'urgent', label: 'Urgent', icon: 'error', color: '#f44336' }
  ];

  faqs = [
    {
      question: 'How do I reset my password?',
      answer: 'You can reset your password by clicking on the "Forgot Password" link on the login page. Follow the instructions sent to your registered email.'
    },
    {
      question: 'What are the transaction limits?',
      answer: 'The maximum transaction limit is ₹100,000 per transaction. For higher limits, please visit your nearest branch.'
    },
    {
      question: 'How do I report unauthorized transactions?',
      answer: 'If you notice any unauthorized transactions, immediately call our 24/7 helpline at 1800-XXX-XXXX or use the emergency form in this support page.'
    },
    {
      question: 'What are the banking hours?',
      answer: 'Our branches are open Monday to Friday, 9:30 AM to 4:30 PM. Online banking services are available 24/7.'
    },
    {
      question: 'How secure is CloudVault?',
      answer: 'We employ industry-leading security measures including 256-bit encryption, two-factor authentication, and real-time fraud detection systems.'
    }
  ];

  emergencyContacts = [
    { title: '24/7 Customer Care', number: '1800-123-4567' },
    { title: 'Card Lost/Stolen', number: '1800-987-6543' },
    { title: 'Fraud & Security Helpline', number: '1800-222-3333' }
  ];

  selectedCategory: string = 'general';

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    this.supportForm = this.fb.group({
      category: ['general', Validators.required],
      subject: ['', [Validators.required, Validators.minLength(5)]],
      description: ['', [Validators.required, Validators.minLength(20)]],
      priority: ['medium', Validators.required]
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files?.length) {
      const file = input.files[0];
      if (file.size <= 5 * 1024 * 1024) { // 5MB limit
        this.selectedFile = file;
      } else {
        this.snackBar.open('File size exceeds 5MB limit', 'Close', {
          duration: 3000,
          horizontalPosition: 'center',
          verticalPosition: 'bottom'
        });
      }
    }
  }

  onSubmit() {
    if (this.supportForm.valid) {
      this.loading = true;
      const formData = new FormData();
      formData.append('category', this.supportForm.get('category')?.value);
      formData.append('subject', this.supportForm.get('subject')?.value);
      formData.append('description', this.supportForm.get('description')?.value);
      formData.append('priority', this.supportForm.get('priority')?.value);
      if (this.selectedFile) {
        formData.append('attachment', this.selectedFile);
      }
      // Simulate API call
      setTimeout(() => {
        this.loading = false;
        this.confirmationDetails = {
          category: this.supportForm.get('category')?.value,
          subject: this.supportForm.get('subject')?.value,
          description: this.supportForm.get('description')?.value,
          priority: this.supportForm.get('priority')?.value
        };
        this.showConfirmation = true;
        this.supportForm.reset({
          category: 'general',
          priority: 'medium'
        });
        this.selectedFile = null;
      }, 1500);
    }
  }

  closeConfirmation() {
    this.showConfirmation = false;
    this.confirmationDetails = null;
  }

  getCategoryIcon(category: string): string {
    const icons: { [key: string]: string } = {
      general: 'help_outline',
      account: 'account_balance',
      transaction: 'payment',
      technical: 'computer',
      security: 'security'
    };
    return icons[category] || 'help_outline';
  }
  logout() {
    const confirmed = confirm('Do you want to exit?');
    if (confirmed) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}


















// import { Component } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { Router } from '@angular/router';


// @Component({
//   selector: 'app-support',
//   standalone: false,
//   templateUrl: './support.component.html',
//   styleUrl: './support.component.css'
// })
// export class SupportComponent {
//   supportForm: FormGroup;
//   isLoading = false;
//   success: string | null = null;
//   error: string | null = null;

//   constructor(private fb: FormBuilder,private router: Router) {
//     this.supportForm = this.fb.group({
//       name: ['', Validators.required],
//       email: ['', [Validators.required, Validators.email]],
//       message: ['', Validators.required]
//     });
//   }

//   onSubmit(): void {
//     if (this.supportForm.invalid) return;
//     this.isLoading = true;
//     this.success = null;
//     this.error = null;
//     // Simulate sending message
//     setTimeout(() => {
//       this.isLoading = false;
//       this.success = 'Your message has been sent! Our team will contact you soon.';
//       this.supportForm.reset();
//     }, 1200);
//   }
//   logout() {
//     const confirmed = confirm('Do you want to exit?');
//     if (confirmed) {
//       localStorage.clear();
//       this.router.navigate(['/']);
//     }
//   }
// }
