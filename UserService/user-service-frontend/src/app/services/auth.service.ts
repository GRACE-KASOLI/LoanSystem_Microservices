import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:9097/users'; // User Service API

  private router = inject(Router);
  private http = inject(HttpClient);

  login(email: string, password: string): Observable<any> {
    return new Observable((observer) => {
      this.http.post(`${this.apiUrl}/login`, { email, password }).subscribe(
        (user: any) => {
          // Redirect to User List Page after login
          this.router.navigate(['/user-list']);
          observer.next(user);
          observer.complete();
        },
        (error) => observer.error(error)
      );
    });
  }

  signup(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }
}
