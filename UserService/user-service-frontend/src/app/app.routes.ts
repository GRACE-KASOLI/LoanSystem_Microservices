import { Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from './services/auth.service';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./components/auth/signup/signup.component').then(
        (m) => m.SignupComponent
      ),
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./components/auth/login/login.component').then(
        (m) => m.LoginComponent
      ),
  },
  {
    path: 'user-list',
    loadComponent: () =>
      import('./components/user-list/user-list.component').then(
        (m) => m.UserListComponent
      ),
  },
];

export const appProviders = [
  AuthService,
  provideHttpClient(),
];
