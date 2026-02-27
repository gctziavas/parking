package com.spacedrop.payment.mcp;

import com.spacedrop.payment.model.Payment;
import com.spacedrop.payment.model.Payment.PaymentMethod;
import com.spacedrop.payment.service.PaymentService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMcpTools {

    private final PaymentService paymentService;

    public PaymentMcpTools(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Tool(description = "Get payment details by payment ID.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PaymentInfo getPaymentById(
            @ToolParam(description = "The unique payment ID") Long paymentId) {
        return paymentService.getPaymentById(paymentId)
                .map(this::toInfo)
                .orElse(null);
    }

    @Tool(description = "Get payment details by booking ID.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PaymentInfo getPaymentByBookingId(
            @ToolParam(description = "The booking ID to get payment for") Long bookingId) {
        return paymentService.getPaymentByBookingId(bookingId)
                .map(this::toInfo)
                .orElse(null);
    }

    @Tool(description = "Get all payments for a specific user.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<PaymentInfo> getUserPayments(
            @ToolParam(description = "The user ID to get payments for") Long userId) {
        return paymentService.getPaymentsByUser(userId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Initiate a new payment for a booking.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public PaymentInfo initiatePayment(
            @ToolParam(description = "The booking ID this payment is for") Long bookingId,
            @ToolParam(description = "The user ID making the payment") Long userId,
            @ToolParam(description = "The payment amount") Double amount,
            @ToolParam(description = "Payment method: CREDIT_CARD, DEBIT_CARD, WALLET, or BANK_TRANSFER") String paymentMethod) {
        
        Payment payment = new Payment();
        payment.setBookingId(bookingId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setMethod(PaymentMethod.valueOf(paymentMethod));
        
        Payment saved = paymentService.initiatePayment(payment);
        return toInfo(saved);
    }

    @Tool(description = "Mark a payment as completed/successful.")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentInfo completePayment(
            @ToolParam(description = "The payment ID to complete") Long paymentId) {
        Payment completed = paymentService.completePayment(paymentId);
        return toInfo(completed);
    }

    @Tool(description = "Mark a payment as failed.")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentInfo failPayment(
            @ToolParam(description = "The payment ID to mark as failed") Long paymentId) {
        Payment failed = paymentService.failPayment(paymentId);
        return toInfo(failed);
    }

    @Tool(description = "Process a refund for a payment.")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentInfo refundPayment(
            @ToolParam(description = "The payment ID to refund") Long paymentId) {
        Payment refunded = paymentService.refundPayment(paymentId);
        return toInfo(refunded);
    }

    private PaymentInfo toInfo(Payment payment) {
        return new PaymentInfo(
                payment.getId(),
                payment.getBookingId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getMethod() != null ? payment.getMethod().name() : null,
                payment.getStatus().name(),
                payment.getTransactionId(),
                payment.getCreatedAt() != null ? payment.getCreatedAt().toString() : null,
                payment.getUpdatedAt() != null ? payment.getUpdatedAt().toString() : null
        );
    }

    public record PaymentInfo(
            Long id,
            Long bookingId,
            Long userId,
            Double amount,
            String paymentMethod,
            String status,
            String transactionId,
            String createdAt,
            String updatedAt
    ) {}
}
