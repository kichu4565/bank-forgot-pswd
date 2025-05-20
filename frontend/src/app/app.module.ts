import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { LoadingComponent } from './components/loading/loading.component';
import { HomeComponent } from './components/home/home.component';
// import { TestConnectionComponent } from './components/test-connection/test-connection.component';
import { ApiService } from './services/api.service';
import { TransactionComponent } from './components/transaction/transaction.component';
import { FixedDepositComponent } from './components/fixed-deposit/fixed-deposit.component';
import { StatementComponent } from './components/statement/statement.component';
import { SupportComponent } from './components/support/support.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AuthInterceptor } from './services/auth.interceptor';
import { ForgotPasswordComponent } from './components/reset-password/forgot-password.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    LoginComponent,
    RegisterComponent,
    LoadingComponent,
    HomeComponent,
    // TestConnectionComponent,
    TransactionComponent,
    FixedDepositComponent,
    StatementComponent,
    SupportComponent,
    ProfileComponent,
    ForgotPasswordComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    RouterModule,
    NgbModule,
    MatDatepickerModule,
    MatInputModule,
    MatFormFieldModule,
    MatNativeDateModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
            positionClass: 'toast-top-center',
            timeOut: 60000,
      closeButton: true,

}),
  ],
  providers: [
    ApiService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
