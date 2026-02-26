package com.spacedrop.payment.service;

import com.spacedrop.payment.config.KafkaConfig;
import com.spacedrop.payment.event.PaymentEvent;
import com.spacedrop.payment.model.Payment;
import com.spacedrop.payment.model.Payment.PaymentStatus;
import com.spacedrop.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    public PaymentService(PaymentRepository paymentRepository,
                          KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.paymentRepository = paymentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Payment initiatePayment(Payment payment) {
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId(UUID.randomUUID().toString());
        Payment saved = paymentRepository.save(payment);
        publishEvent("PAYMENT_INITIATED", saved);
        log.info("Payment initiated: {} for booking {}", saved.getId(), saved.getBookingId());
        return saved;
    }

    public Payment completePayment(Long id) {
        return updateStatus(id, PaymentStatus.COMPLETED, "PAYMENT_COMPLETED");
    }

    public Payment failPayment(Long id) {
        return updateStatus(id, PaymentStatus.FAILED, "PAYMENT_FAILED");
    }

    public Payment refundPayment(Long id) {
        return updateStatus(id, PaymentStatus.REFUNDED, "PAYMENT_REFUNDED");
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    private Payment updateStatus(Long id, PaymentStatus status, String eventType) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
        payment.setStatus(status);
        payment.setUpdatedAt(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);
        publishEvent(eventType, saved);
        log.info("Payment {} status updated to {}", id, status);
        return saved;
    }

    private void publishEvent(String type, Payment payment) {
        PaymentEvent event = new PaymentEvent(type, payment.getId(), payment.getBookingId(),
                payment.getUserId(), payment.getAmount(), payment.getStatus().name());
        kafkaTemplate.send(KafkaConfig.PAYMENT_EVENTS_TOPIC, payment.getId().toString(), event);
    }
}
