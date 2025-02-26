package com.example.LoanService;

import com.example.LoanService.Loan;
import com.example.LoanService.LoanStatus;
import com.example.LoanService.User;
import com.example.LoanService.LoanRepository;
import com.example.LoanService.MpesaService;
import com.example.LoanService.NotificationClient;
import com.example.LoanService.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final MpesaService mpesaService;
    private final NotificationClient notificationClient;
    private final UserClient userClient;

    @Autowired
    public LoanService(LoanRepository loanRepository,
                       MpesaService mpesaService,
                       NotificationClient notificationClient,
                       UserClient userClient) {
        this.loanRepository = loanRepository;
        this.mpesaService = mpesaService;
        this.notificationClient = notificationClient;
        this.userClient = userClient;
    }

    public Loan applyForLoan(Loan loan) {
        // First, verify the user exists using the User Service
        User user = userClient.getUserById(loan.getUserId());
        if (user == null) {
            throw new RuntimeException("User with ID " + loan.getUserId() + " not found");
        }
        // Optionally, you can log or update loan details based on user data here

        // Save the loan
        Loan savedLoan = loanRepository.save(loan);

        // If the loan is approved, trigger disbursement and send a notification
        if (savedLoan.getStatus() == LoanStatus.APPROVED) {
            String mpesaResponse = mpesaService.requestLoanDisbursement(savedLoan.getPhoneNumber(), savedLoan.getAmount().toString());
            System.out.println("M-Pesa Response: " + mpesaResponse);

            String notificationResponse = notificationClient.sendNotification(
                    savedLoan.getUserId(),
                    savedLoan.getId(),
                    "Your loan of " + savedLoan.getAmount() + " has been approved and disbursed."
            );
            System.out.println("Notification Response: " + notificationResponse);
        }
        return savedLoan;
    }

    public Optional<Loan> getLoanById(Long loanId) {
        return loanRepository.findById(loanId);
    }

    public Optional<Long> getActiveLoanIdByphoneNumber(String phoneNumber) {
        return loanRepository.findActiveLoanIdByphoneNumber(phoneNumber);
    }

    public List<Loan> getUserLoans(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    public Loan updateLoanStatus(Long loanId, String status) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isPresent()) {
            Loan loan = loanOpt.get();
            try {
                LoanStatus newStatus = LoanStatus.valueOf(status.toUpperCase());
                loan.setStatus(newStatus);
                Loan updatedLoan = loanRepository.save(loan);

                if (newStatus == LoanStatus.APPROVED) {
                    String mpesaResponse = mpesaService.requestLoanDisbursement(loan.getPhoneNumber(), loan.getAmount().toString());
                    System.out.println("M-Pesa Response: " + mpesaResponse);

                    String notificationResponse = notificationClient.sendNotification(
                            loan.getUserId(),
                            loan.getId(),
                            "Your loan has been approved and disbursed."
                    );
                    System.out.println("Notification Response: " + notificationResponse);
                }
                return updatedLoan;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid loan status: " + status);
            }
        }
        throw new RuntimeException("Loan not found with ID: " + loanId);
    }
}
