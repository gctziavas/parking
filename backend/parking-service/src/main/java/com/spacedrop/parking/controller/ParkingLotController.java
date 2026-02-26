package com.spacedrop.parking.controller;

import com.spacedrop.parking.model.ParkingLot;
import com.spacedrop.parking.service.ParkingLotService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @PostMapping
    public ResponseEntity<ParkingLot> createParkingLot(@Valid @RequestBody ParkingLot parkingLot) {
        ParkingLot created = parkingLotService.createParkingLot(parkingLot);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        return ResponseEntity.ok(parkingLotService.getActiveParkingLots());
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingLot>> getAvailableParkingLots() {
        return ResponseEntity.ok(parkingLotService.getAvailableParkingLots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Long id) {
        return parkingLotService.getParkingLotById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ParkingLot>> getParkingLotsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(parkingLotService.getParkingLotsByOwner(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLot> updateParkingLot(@PathVariable Long id,
                                                       @Valid @RequestBody ParkingLot parkingLot) {
        ParkingLot updated = parkingLotService.updateParkingLot(id, parkingLot);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
        return ResponseEntity.noContent().build();
    }
}
