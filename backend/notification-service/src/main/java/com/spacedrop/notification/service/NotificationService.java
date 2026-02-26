package com.spacedrop.notification.service;

import com.spacedrop.notification.event.BookingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    @KafkaListener(topics = "booking-events", groupId = "notification-service")
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received booking event: type={}, bookingId={}, userId={}, status={}",
                event.getType(), event.getBookingId(), event.getUserId(), event.getStatus());

        switch (event.getType()) {
            case "BOOKING_CREATED":
                log.info("Sending booking confirmation notification to user {}", event.getUserId());
                break;
            case "BOOKING_CONFIRMED":
                log.info("Sending booking confirmed notification to user {}", event.getUserId());
                break;
            case "BOOKING_CANCELLED":
                log.info("Sending booking cancellation notification to user {}", event.getUserId());
                break;
            case "BOOKING_COMPLETED":
                log.info("Sending booking completion notification to user {}", event.getUserId());
                break;
            default:
                log.warn("Unknown booking event type: {}", event.getType());
        }
    }
}
