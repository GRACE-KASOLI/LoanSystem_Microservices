import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CurrencyPipe } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { NgChartsModule } from 'ng2-charts'; // Import NgChartsModule
import { ChartConfiguration, ChartType } from 'chart.js'; // Import Chart.js types

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, RouterModule, NgChartsModule], // Add NgChartsModule here
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  dashboardData: any = {};
   errorMessage: string = ''; // Declare the errorMessage property

  // Chart Configuration
  public loanChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false, // Ensures the chart does not stretch
    aspectRatio: 1.2 // Adjusts the size of the chart to be slightly bigger
  };

  public loanChartData = {
    labels: ['ACTIVE', 'APPROVED', 'DISBURSED', 'PENDING'],
    datasets: [{
      data: [0, 0, 0, 0],
      backgroundColor: ['#4CAF50', '#2196F3', '#FFC107', '#FF5722']
    }]
  };

  public loanChartType: ChartType = 'pie';

  constructor(private http: HttpClient, private router: Router) {}

 ngOnInit(): void {
   this.fetchDashboardSummary();
   this.fetchLoanChartData();
 }

 fetchDashboardSummary() {
   this.http.get('http://localhost:9093/dashboard').subscribe(
     (data: any) => {
       console.log('Dashboard Data:', data);
       this.dashboardData = data;
     },
     (error) => console.error('Error fetching dashboard summary', error)
   );
 }

 fetchLoanChartData() {
   this.http.get('http://localhost:9093/loans').subscribe(
     (data: any) => {
       console.log('Loan Chart Data:', data);

       // Initialize counters
       let activeLoans = 0;
       let approvedLoans = 0;
       let disbursedLoans = 0;
       let pendingLoans = 0;

       // Iterate over the loan data and count each status
       data.forEach((loan: any) => {
         switch (loan.status) {
           case 'ACTIVE':
             activeLoans++;
             break;
           case 'APPROVED':
             approvedLoans++;
             break;
           case 'DISBURSED':
             disbursedLoans++;
             break;
           case 'PENDING':
             pendingLoans++;
             break;
         }
       });

       // Update the loan chart data
       this.loanChartData = {
         labels: ['ACTIVE', 'APPROVED', 'DISBURSED', 'PENDING'],
         datasets: [{
           data: [activeLoans, approvedLoans, disbursedLoans, pendingLoans],
           backgroundColor: ['#4CAF50', '#2196F3', '#FFC107', '#FF5722']
         }]
       };
     },
     (error) => {
       console.error('Error fetching loan chart data', error);
       this.errorMessage = 'Failed to load loan data. Please try again later.';
     }
   );
 }

  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
