import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'bank-app';
}

// import { Component } from '@angular/core';
// // import { RouterOutlet } from '@angular/router';

// @Component({
//   selector: 'app-root',
//   // imports: [RouterOutlet],
//   templateUrl: './app.component.html',
//   styleUrls: ['./app.component.css']
// })
// export class AppComponent {
//   title = 'bank-management';
// }