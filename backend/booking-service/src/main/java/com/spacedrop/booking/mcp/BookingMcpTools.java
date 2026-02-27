package com.spacedrop.booking.mcp;

import com.spacedrop.booking.model.Booking;
import com.spacedrop.booking.model.Booking.BookingStatus;
import com.spacedrop.booking.service.BookingService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingMcpTools {

    private final BookingService bookingService;

    public BookingMcpTools(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Tool(description = "Create a new parking booking for a user at a specific parking lot.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingInfo createBooking(
            @ToolParam(description = "The user ID making the booking") Long userId,
            @ToolParam(description = "The parking lot ID to book") Long parkingLotId,
            @ToolParam(description = "Start time in ISO format (e.g., 2024-01-15T10:00:00)") String startTime,
            @ToolParam(description = "End time in ISO format (e.g., 2024-01-15T14:00:00)") String endTime,
            @ToolParam(description = "Total price for the booking") Double totalPrice) {
        
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setParkingLotId(parkingLotId);
        booking.setStartTime(LocalDateTime.parse(startTime));
        booking.setEndTime(LocalDateTime.parse(endTime));
        booking.setTotalPrice(totalPrice);
        
        Booking saved = bookingService.createBooking(booking);
        return toInfo(saved);
    }

    @Tool(description = "Get details of a specific booking by its ID.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingInfo getBookingDetails(
            @ToolParam(description = "The unique booking ID") Long bookingId) {
        return bookingService.getBookingById(bookingId)
                .map(this::toInfo)
                .orElse(null);
    }

    @Tool(description = "Get all bookings for a specific user.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookingInfo> getUserBookings(
            @ToolParam(description = "The user ID to get bookings for") Long userId) {
        return bookingService.getBookingsByUser(userId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Get all bookings for a specific parking lot.")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public List<BookingInfo> getParkingLotBookings(
            @ToolParam(description = "The parking lot ID to get bookings for") Long parkingLotId) {
        return bookingService.getBookingsByParkingLot(parkingLotId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Confirm a pending booking.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingInfo confirmBooking(
            @ToolParam(description = "The booking ID to confirm") Long bookingId) {
        Booking confirmed = bookingService.confirmBooking(bookingId);
        return toInfo(confirmed);
    }

    @Tool(description = "Cancel an existing booking.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingInfo cancelBooking(
            @ToolParam(description = "The booking ID to cancel") Long bookingId) {
        Booking cancelled = bookingService.cancelBooking(bookingId);
        return toInfo(cancelled);
    }

    @Tool(description = "Mark a booking as completed.")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public BookingInfo completeBooking(
            @ToolParam(description = "The booking ID to mark as completed") Long bookingId) {
        Booking completed = bookingService.completeBooking(bookingId);
        return toInfo(completed);
    }

    private BookingInfo toInfo(Booking booking) {
        return new BookingInfo(
                booking.getId(),
                booking.getUserId(),
                booking.getParkingLotId(),
                booking.getStartTime().toString(),
                booking.getEndTime().toString(),
                booking.getStatus().name(),
                booking.getTotalPrice()
        );
    }

    public record BookingInfo(
            Long id,
            Long userId,
            Long parkingLotId,
            String startTime,
            String endTime,
            String status,
            Double totalPrice
    ) {}
}
