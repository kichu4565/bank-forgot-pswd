import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  accountDetails: any = null;
  isLoading = false;
  error = '';
  accountNumber = '';

  constructor(private apiService: ApiService, private router: Router) {}

  ngOnInit(): void {
    this.accountNumber = localStorage.getItem('accountNumber') || '';
    if (this.accountNumber) {
      this.isLoading = true;
      this.apiService.getAccountDetails(this.accountNumber).subscribe({
        next: (data) => {
          this.accountDetails = data;
          this.isLoading = false;
        },
        error: (err) => {
          this.error = err.error?.message || 'Failed to load profile.';
          this.isLoading = false;
        }
      });
    }
  }
  logout() {
    const confirmed = confirm('Do you want to exit?');
    if (confirmed) {
      localStorage.clear();
      this.router.navigate(['/']);
    }
  }
}
