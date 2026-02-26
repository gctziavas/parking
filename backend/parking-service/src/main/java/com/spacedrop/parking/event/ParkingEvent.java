package com.spacedrop.parking.event;

public class ParkingEvent {

    private String type;
    private Long parkingLotId;
    private Integer availableSpots;

    public ParkingEvent() {
    }

    public ParkingEvent(String type, Long parkingLotId, Integer availableSpots) {
        this.type = type;
        this.parkingLotId = parkingLotId;
        this.availableSpots = availableSpots;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }
}
