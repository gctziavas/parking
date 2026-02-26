package com.spacedrop.payment.repository;

import com.spacedrop.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByUserId(Long userId);

    Optional<Payment> findByBookingId(Long bookingId);

    List<Payment> findByStatus(Payment.PaymentStatus status);
}
