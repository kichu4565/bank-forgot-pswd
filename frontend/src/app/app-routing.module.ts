import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { LoadingComponent } from './components/loading/loading.component';
import { TransactionComponent } from './components/transaction/transaction.component';
import { FixedDepositComponent } from './components/fixed-deposit/fixed-deposit.component';
import { StatementComponent } from './components/statement/statement.component';
import { ProfileComponent } from './components/profile/profile.component';
import { SupportComponent } from './components/support/support.component';
import { ForgotPasswordComponent } from './components/reset-password/forgot-password.component';

import { AuthGuard } from './services/auth.guard';
import { GuestGuard } from './services/guest.guard';

const routes: Routes = [
  // routes for guests only
  { path: '', component: WelcomeComponent, canActivate: [GuestGuard] },
  { path: 'login', component: LoginComponent, canActivate: [GuestGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [GuestGuard] },

  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: 'transaction', component: TransactionComponent, canActivate: [AuthGuard] },
  { path: 'fixed-deposit', component: FixedDepositComponent, canActivate: [AuthGuard] },
  { path: 'statement', component: StatementComponent, canActivate: [AuthGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'loading', component: LoadingComponent, canActivate: [AuthGuard] },

  { path: 'support', component: SupportComponent },
  { path: 'forgot', component: ForgotPasswordComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
