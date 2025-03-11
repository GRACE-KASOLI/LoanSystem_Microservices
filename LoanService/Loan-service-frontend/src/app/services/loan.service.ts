import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class LoanService {
  private apiUrl = 'http://localhost:9093/loans';

  private loanUpdatedSource = new Subject<void>();
  loanUpdated$ = this.loanUpdatedSource.asObservable();

  constructor(private http: HttpClient) {}

  applyLoan(loanData: any): Observable<any> {
    console.log('Applying for loan:', loanData); // ✅ Log loan data before sending
    return this.http.post(`${this.apiUrl}/apply`, loanData).pipe(
      tap(() => {
        console.log('Loan successfully applied, refreshing list'); // ✅ Log success
        this.getLoans(loanData.userId).subscribe((loans) => {
          this.notifyLoanListUpdate();
        });
      })
    );
  }

  getLoans(userId?: string): Observable<any[]> {
    const url = userId ? `${this.apiUrl}/user/${userId}` : this.apiUrl;
    return this.http.get<any[]>(url);
  }

  getAllLoans(): Observable<any[]> { // ✅ Added method to fetch all loans
    return this.http.get<any[]>(this.apiUrl);
  }

  getLoanById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  updateLoanStatus(loanId: number, status: string): Observable<any> {
    console.log(`Updating loan ${loanId} to status ${status}`); // ✅ Log update request
    return this.http.put(`${this.apiUrl}/${loanId}/status?status=${status}`, {}).pipe(
      tap(() => this.notifyLoanListUpdate()) // ✅ Notify list update
    );
  }

  updateLoan(loan: any): Observable<any> { // ✅ Added updateLoan method
    console.log('Updating loan:', loan); // ✅ Log update request
    return this.http.put(`${this.apiUrl}/${loan.id}`, loan).pipe(
      tap(() => this.notifyLoanListUpdate())
    );
  }

  deleteLoan(id: number): Observable<any> { // ✅ Added deleteLoan method
    return this.http.delete(`${this.apiUrl}/${id}`).pipe(
      tap(() => this.notifyLoanListUpdate())
    );
  }

  notifyLoanListUpdate() {
    this.loanUpdatedSource.next();
  }
}
