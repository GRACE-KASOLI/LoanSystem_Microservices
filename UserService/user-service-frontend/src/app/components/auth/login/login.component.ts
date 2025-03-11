import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  private authService: AuthService = inject(AuthService);
  private router: Router = inject(Router);

  login() {
    this.authService.login(this.email, this.password).subscribe({
      next: (response: any) => {
        const userRole = response?.role; // Extract role from response

        // ✅ Redirect based on user role (only handling User Service for now)
        if (userRole === 'ADMIN' || userRole === 'MEMBER') {
          this.router.navigate(['/user-list']); // Redirect to User List
        } else {
          this.errorMessage = 'User role not recognized';
        }
      },
      error: (err: any) => {
        this.errorMessage = err.error?.message || 'Login failed! Please check your credentials.';
      },
    });
  }
}
