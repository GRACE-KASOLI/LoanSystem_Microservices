import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { LoanService } from '../../services/loan.service';
import { Inject } from '@angular/core';

@Component({
  selector: 'app-loan-update',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './loan-update.component.html',
  styleUrls: ['./loan-update.component.css']
})
export class LoanUpdateComponent {
  loan: any = {};

  constructor(@Inject(LoanService) private loanService: LoanService) {}

  updateLoan() {
    if (!this.loan || !this.loan.id) {
      console.error('Loan object or ID is missing');
      return;
    }

    this.loanService.updateLoan(this.loan).subscribe(() => {
      console.log('Loan updated successfully');
      // Redirect to loan list after update instead of fetchLoans
      window.location.reload();
    },
    (error: any) => {
      console.error('Error updating loan:', error);
    });
  }
}
