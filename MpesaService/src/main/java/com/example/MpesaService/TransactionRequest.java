// In MpesaService package
package com.example.MpesaService;

import java.math.BigDecimal;

public class TransactionRequest {
    private Long loanId;
    private String phoneNumber;
    private BigDecimal amount;
    private String type;

    // Getters and setters
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
