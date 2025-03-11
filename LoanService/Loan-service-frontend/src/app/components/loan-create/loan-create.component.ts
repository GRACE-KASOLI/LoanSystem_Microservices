import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoanService } from '../../services/loan.service';

@Component({
  selector: 'app-loan-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  providers: [LoanService],
  templateUrl: './loan-create.component.html',
  styleUrls: ['./loan-create.component.css']
})
export class LoanCreateComponent {
  loanForm: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  isSubmitting = false;

  constructor(private fb: FormBuilder, private loanService: LoanService) {
    this.loanForm = this.fb.group({
      userId: ['', Validators.required],
      amount: ['', [Validators.required, Validators.min(1)]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^\\+?[0-9]{10,15}$')]], // Valid phone number
      interestRate: [5, [Validators.required, Validators.min(0)]], // Default interest rate
      totalAmountDue: [0, Validators.required], // Default 0, backend calculates it later
    });
  }

  applyLoan() {
    if (this.loanForm.valid) {
      this.isSubmitting = true;
      console.log("Submitting Loan Data:", this.loanForm.value);

      this.loanService.applyLoan(this.loanForm.value).subscribe(
        (response) => {
          console.log("Loan application success:", response);
          this.successMessage = 'Loan Application Submitted Successfully!';
          this.errorMessage = '';
          this.loanForm.reset();
          this.loanService.notifyLoanListUpdate();
          setTimeout(() => (this.successMessage = ''), 3000);
          this.isSubmitting = false;
        },
        (error) => {
          console.error('Loan application failed:', error);
          this.errorMessage = 'Loan Application Failed. Please try again.';
          this.isSubmitting = false;
        }
      );
    } else {
      this.errorMessage = 'Please fill in all required fields correctly.';
    }
  }
}
