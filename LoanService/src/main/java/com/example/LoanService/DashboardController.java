package com.example.LoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import com.example.LoanService.LoanService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private LoanService loanService; // Added instance of LoanService

    @GetMapping
    public DashboardResponse getDashboardStats() {
        Long totalLoans = loanRepository.count();
        Long activeLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);
        BigDecimal totalDisbursed = loanRepository.getTotalDisbursedAmount();
        BigDecimal totalRepaid = repaymentRepository.getTotalRepaidAmount();

        return new DashboardResponse(totalLoans, activeLoans, totalDisbursed, totalRepaid);
    }

    @GetMapping("/count") // Changed endpoint to avoid duplicate @GetMapping
    public ResponseEntity<Map<String, Long>> getDashboardData() {
        Map<String, Long> dashboardData = new HashMap<>();

        dashboardData.put("ACTIVE", loanService.countLoansByStatus("ACTIVE"));
        dashboardData.put("APPROVED", loanService.countLoansByStatus("APPROVED"));
        dashboardData.put("DISBURSED", loanService.countLoansByStatus("DISBURSED"));
        dashboardData.put("PENDING", loanService.countLoansByStatus("PENDING"));

        return ResponseEntity.ok(dashboardData);
    }
}
