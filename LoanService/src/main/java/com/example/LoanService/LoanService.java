package com.example.LoanService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

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
        logger.info("📌 Applying for Loan: {}", loan);

        User user = userClient.getUserById(loan.getUserId());
        if (user == null) {
            logger.error("User with ID {} not found", loan.getUserId());
            throw new RuntimeException("User with ID " + loan.getUserId() + " not found");
        }

        Loan savedLoan = loanRepository.save(loan);
        logger.info("✅ Loan saved: {}", savedLoan);

        if (savedLoan.getStatus() == LoanStatus.APPROVED) {
            logger.info("📌 Sending disbursement request to MpesaService...");
            try {
                String mpesaResponse = mpesaService.requestLoanDisbursement(savedLoan.getPhoneNumber(), savedLoan.getAmount().toString());
                logger.info("🔹 M-Pesa Response: {}", mpesaResponse);
            } catch (Exception e) {
                logger.error("Failed to request loan disbursement via M-Pesa: {}", e.getMessage());
            }
        }

        return savedLoan;
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getLoanById(Long loanId) {
        return loanRepository.findById(loanId);
    }

    public Optional<Long> getActiveLoanIdByphoneNumber(String phoneNumber) {
        return loanRepository.findActiveLoanIdByphoneNumber(phoneNumber);
    }
    public long countLoansByStatus(String status) {
        return loanRepository.countByStatus(status);
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
                logger.info("Loan {} updated to status {}", loanId, newStatus);

                if (newStatus == LoanStatus.APPROVED) {
                    try {
                        String mpesaResponse = mpesaService.requestLoanDisbursement(loan.getPhoneNumber(), loan.getAmount().toString());
                        logger.info("M-Pesa Response: {}", mpesaResponse);
                    } catch (Exception e) {
                        logger.error("Error during M-Pesa disbursement: {}", e.getMessage());
                    }

                    try {
                        String notificationResponse = notificationClient.sendNotification(
                                loan.getUserId(),
                                loan.getId(),
                                "Your loan has been approved and disbursed."
                        );
                        logger.info("Notification Response: {}", notificationResponse);
                    } catch (Exception e) {
                        logger.error("Error sending notification: {}", e.getMessage());
                    }
                }
                return updatedLoan;
            } catch (IllegalArgumentException e) {
                logger.error("Invalid loan status: {}", status);
                throw new IllegalArgumentException("Invalid loan status: " + status);
            }
        }
        logger.error("Loan not found with ID: {}", loanId);
        throw new RuntimeException("Loan not found with ID: " + loanId);
    }
    public Loan updateLoan(Long loanId, Loan updatedLoan) {
        return loanRepository.findById(loanId)
                .map(existingLoan -> {
                    existingLoan.setAmount(updatedLoan.getAmount());
                    existingLoan.setStatus(updatedLoan.getStatus());
                    existingLoan.setPhoneNumber(updatedLoan.getPhoneNumber());
                    return loanRepository.save(existingLoan);
                })
                .orElseThrow(() -> new RuntimeException("Loan not found with ID: " + loanId));
    }
    public void deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new RuntimeException("Loan not found with ID: " + loanId);
        }
        loanRepository.deleteById(loanId);
    }

}
