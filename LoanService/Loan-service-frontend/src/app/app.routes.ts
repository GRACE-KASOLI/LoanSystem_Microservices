import { Routes } from '@angular/router';
import { LoanListComponent } from './components/loan-list/loan-list.component';
import { LoanCreateComponent } from './components/loan-create/loan-create.component';
import { LoanUpdateComponent } from './components/loan-update/loan-update.component';
import { LoanDetailsComponent } from './components/loan-details/loan-details.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'loan-list', component: LoanListComponent },
  { path: 'loan-create', component: LoanCreateComponent },
  { path: 'loan-update/:id', component: LoanUpdateComponent },
  { path: 'loan-details/:id', component: LoanDetailsComponent },
  { path: '**', redirectTo: 'dashboard' }
];
