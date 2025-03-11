import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';  // Import RouterModule here
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [CommonModule, FormsModule,  RouterModule],
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
})
export class SignupComponent {
  name: string = '';
  email: string = '';
  phone: string = '';
  password: string = '';
  role: string = 'MEMBER';
  errorMessage: string = '';

  private authService: AuthService = inject(AuthService); // Explicit type
  private router: Router = inject(Router); // Explicit type

  signup() {
    const user = {
      name: this.name,
      email: this.email,
      phone: this.phone,
      password: this.password,
      role: this.role,
    };

    this.authService.signup(user).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err: any) => { // Explicitly type 'err' to avoid TS7006
        this.errorMessage = err.error?.message || 'Signup failed';
      },
    });
  }
}
