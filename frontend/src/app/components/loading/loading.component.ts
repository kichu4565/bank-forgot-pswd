import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-loading',
  templateUrl: './loading.component.html',
  standalone: false,
  styleUrls: ['./loading.component.css']
})
export class LoadingComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    setTimeout(() => {
      this.router.navigate(['/home']); 
    }, 2500); 
  }
}