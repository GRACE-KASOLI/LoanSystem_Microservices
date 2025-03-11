package com.example.LoanService;


import java.math.BigDecimal;

public class DashboardResponse {
    private Long totalLoans;
    private Long activeLoans;
    private BigDecimal totalDisbursed;
    private BigDecimal totalRepaid;

    // Constructors, Getters, and Setters

    public DashboardResponse(Long totalLoans, Long activeLoans, BigDecimal totalDisbursed, BigDecimal totalRepaid) {
        this.totalLoans = totalLoans;
        this.activeLoans = activeLoans;
        this.totalDisbursed = totalDisbursed;
        this.totalRepaid = totalRepaid;
    }

    public Long getTotalLoans() {
        return totalLoans;
    }

    public void setTotalLoans(Long totalLoans) {
        this.totalLoans = totalLoans;
    }

    public Long getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(Long activeLoans) {
        this.activeLoans = activeLoans;
    }

    public BigDecimal getTotalDisbursed() {
        return totalDisbursed;
    }

    public void setTotalDisbursed(BigDecimal totalDisbursed) {
        this.totalDisbursed = totalDisbursed;
    }

    public BigDecimal getTotalRepaid() {
        return totalRepaid;
    }

    public void setTotalRepaid(BigDecimal totalRepaid) {
        this.totalRepaid = totalRepaid;
    }
}

