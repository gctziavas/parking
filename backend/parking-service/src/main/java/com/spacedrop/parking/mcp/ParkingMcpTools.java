package com.spacedrop.parking.mcp;

import com.spacedrop.parking.model.ParkingLot;
import com.spacedrop.parking.service.ParkingLotService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingMcpTools {

    private final ParkingLotService parkingLotService;

    public ParkingMcpTools(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @Tool(description = "Search for available parking lots. Returns a list of parking lots with available spots.")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public List<ParkingLotInfo> searchAvailableParking() {
        return parkingLotService.getAvailableParkingLots().stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Get all active parking lots in the system.")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public List<ParkingLotInfo> getAllActiveParkingLots() {
        return parkingLotService.getActiveParkingLots().stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Get details of a specific parking lot by its ID.")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public ParkingLotInfo getParkingLotDetails(
            @ToolParam(description = "The unique ID of the parking lot") Long parkingLotId) {
        Optional<ParkingLot> lot = parkingLotService.getParkingLotById(parkingLotId);
        return lot.map(this::toInfo).orElse(null);
    }

    @Tool(description = "Search parking lots by city name.")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public List<ParkingLotInfo> searchParkingByCity(
            @ToolParam(description = "The city name to search for parking lots") String city) {
        return parkingLotService.getActiveParkingLots().stream()
                .filter(lot -> lot.getCity().equalsIgnoreCase(city))
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Find parking lots near a location within a radius. Uses approximate distance calculation.")
    @PreAuthorize("hasAnyRole('USER', 'OWNER', 'ADMIN')")
    public List<ParkingLotInfo> findNearbyParking(
            @ToolParam(description = "Latitude of the search location") Double latitude,
            @ToolParam(description = "Longitude of the search location") Double longitude,
            @ToolParam(description = "Search radius in kilometers") Double radiusKm) {
        return parkingLotService.getAvailableParkingLots().stream()
                .filter(lot -> calculateDistance(latitude, longitude, lot.getLatitude(), lot.getLongitude()) <= radiusKm)
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    @Tool(description = "Get parking lots owned by a specific owner.")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public List<ParkingLotInfo> getParkingLotsByOwner(
            @ToolParam(description = "The owner's user ID") Long ownerId) {
        return parkingLotService.getParkingLotsByOwner(ownerId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    private ParkingLotInfo toInfo(ParkingLot lot) {
        return new ParkingLotInfo(
                lot.getId(),
                lot.getName(),
                lot.getFullAddress(),
                lot.getCity(),
                lot.getState(),
                lot.getLatitude(),
                lot.getLongitude(),
                lot.getTotalSpots(),
                lot.getAvailableSpots(),
                lot.getHourlyRate(),
                lot.isActive()
        );
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public record ParkingLotInfo(
            Long id,
            String name,
            String address,
            String city,
            String state,
            Double latitude,
            Double longitude,
            Integer totalSpots,
            Integer availableSpots,
            Double hourlyRate,
            boolean active
    ) {}
}
