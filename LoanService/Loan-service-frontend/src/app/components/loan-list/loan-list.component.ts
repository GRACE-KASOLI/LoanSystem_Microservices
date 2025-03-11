import { Component, OnInit, OnDestroy } from '@angular/core'; // ✅ Added OnDestroy
import { CommonModule } from '@angular/common';
import { LoanService } from '../../services/loan.service';
import { Subscription } from 'rxjs'; // ✅ Added Subscription

@Component({
  selector: 'app-loan-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.css']
})
export class LoanListComponent implements OnInit, OnDestroy { // ✅ Implement OnDestroy
  loans: any[] = [];
  errorMessage: string = '';
  private loanSubscription!: Subscription; // ✅ Added Subscription variable

  constructor(private loanService: LoanService) {}

  ngOnInit() {
    const userId = '';
    this.fetchLoans(userId);
    this.loanSubscription = this.loanService.loanUpdated$.subscribe(() => this.fetchLoans(userId)); // ✅ Store subscription
  }

  fetchLoans(userId?: string) {
    console.log('Fetching loans for user:', userId); // ✅ Log user ID
    this.loanService.getLoans(userId).subscribe(
      (data: any[]) => {
        console.log('Loans fetched:', data); // ✅ Log fetched loans
        this.loans = data;
      },
      (error: any) => {
        this.errorMessage = 'Failed to load loans. Please try again.';
        console.error('Error fetching loans:', error);
      }
    );
  }

  fetchAllLoans() { // ✅ Added method to fetch all loans
    console.log('Fetching all loans'); // ✅ Log action
    this.loanService.getAllLoans().subscribe(
      (data: any[]) => {
        console.log('All loans fetched:', data); // ✅ Log fetched loans
        this.loans = data;
      },
      (error: any) => {
        this.errorMessage = 'Failed to load all loans. Please try again.';
        console.error('Error fetching all loans:', error);
      }
    );
  }

  updateLoanStatus(loanId: number, newStatus: string) {
    console.log(`Updating loan ${loanId} to status ${newStatus}`);
    this.loanService.updateLoanStatus(loanId, newStatus).subscribe(
      (updatedLoan: any) => { // ✅ Explicitly type 'updatedLoan'
        console.log('Loan updated successfully:', updatedLoan);
        this.fetchAllLoans(); // Refresh the loan list after update
      },
      (error: any) => {
        this.errorMessage = 'Failed to update loan status. Please try again.';
        console.error('Error updating loan:', error);
      }
    );
  }

  deleteLoan(loanId: number) { // ✅ Delete function
    if (confirm('Are you sure you want to delete this loan?')) {
      console.log(`Deleting loan with ID: ${loanId}`);
      this.loanService.deleteLoan(loanId).subscribe(
        () => {
          console.log('Loan deleted successfully');
          this.fetchAllLoans(); // Refresh the loan list after deletion
        },
        (error: any) => {
          this.errorMessage = 'Failed to delete loan. Please try again.';
          console.error('Error deleting loan:', error);
        }
      );
    }
  }

  updateLoan(loan: any) { // ✅ Added updateLoan method
    console.log('Updating loan:', loan);
    // Implement the logic to navigate or open a form for loan update
  }

  ngOnDestroy() { // ✅ Unsubscribe to prevent memory leaks
    if (this.loanSubscription) {
      this.loanSubscription.unsubscribe();
    }
  }
}
