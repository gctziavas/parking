package com.spacedrop.booking.event;

public class BookingEvent {

    private String type;
    private Long bookingId;
    private Long userId;
    private Long parkingLotId;
    private String status;

    public BookingEvent() {
    }

    public BookingEvent(String type, Long bookingId, Long userId, Long parkingLotId, String status) {
        this.type = type;
        this.bookingId = bookingId;
        this.userId = userId;
        this.parkingLotId = parkingLotId;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
