import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoanService } from '../../services/loan.service';
import { Inject } from '@angular/core';


@Component({
  selector: 'app-loan-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './loan-details.component.html',
  styleUrls: ['./loan-details.component.css']
})
export class LoanDetailsComponent {
  loans: any = {};
  loanId: string = '';

constructor(@Inject(LoanService) private loanService: LoanService) {}

  getLoanById() {
    this.loanService.getLoanById(Number(this.loanId)).subscribe((data: any) => {
      this.loans = data;
    });
  }
}
