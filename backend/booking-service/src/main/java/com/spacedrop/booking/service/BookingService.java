package com.spacedrop.booking.service;

import com.spacedrop.booking.config.KafkaConfig;
import com.spacedrop.booking.event.BookingEvent;
import com.spacedrop.booking.model.Booking;
import com.spacedrop.booking.model.Booking.BookingStatus;
import com.spacedrop.booking.repository.BookingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;

    public BookingService(BookingRepository bookingRepository,
                          KafkaTemplate<String, BookingEvent> kafkaTemplate) {
        this.bookingRepository = bookingRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Booking createBooking(Booking booking) {
        booking.setStatus(BookingStatus.PENDING);
        Booking saved = bookingRepository.save(booking);
        publishEvent("BOOKING_CREATED", saved);
        log.info("Booking created: {}", saved.getId());
        return saved;
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByParkingLot(Long parkingLotId) {
        return bookingRepository.findByParkingLotId(parkingLotId);
    }

    public Booking confirmBooking(Long id) {
        return updateStatus(id, BookingStatus.CONFIRMED, "BOOKING_CONFIRMED");
    }

    public Booking cancelBooking(Long id) {
        return updateStatus(id, BookingStatus.CANCELLED, "BOOKING_CANCELLED");
    }

    public Booking completeBooking(Long id) {
        return updateStatus(id, BookingStatus.COMPLETED, "BOOKING_COMPLETED");
    }

    private Booking updateStatus(Long id, BookingStatus status, String eventType) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
        booking.setStatus(status);
        Booking saved = bookingRepository.save(booking);
        publishEvent(eventType, saved);
        log.info("Booking {} status updated to {}", id, status);
        return saved;
    }

    private void publishEvent(String type, Booking booking) {
        BookingEvent event = new BookingEvent(type, booking.getId(), booking.getUserId(),
                booking.getParkingLotId(), booking.getStatus().name());
        kafkaTemplate.send(KafkaConfig.BOOKING_EVENTS_TOPIC, booking.getId().toString(), event);
    }
}
