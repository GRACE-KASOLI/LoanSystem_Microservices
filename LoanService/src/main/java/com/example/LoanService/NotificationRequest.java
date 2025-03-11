package com.example.LoanService;

public class NotificationRequest {
    private String message;
    private String phoneNumber;
    private Long loanId;
    private Long userId;

    // Constructors
    public NotificationRequest() {}

    public NotificationRequest(String message, String phoneNumber, Long loanId, Long userId) {
        this.message = message;
        this.phoneNumber = phoneNumber;
        this.loanId = loanId;
        this.userId = userId;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
