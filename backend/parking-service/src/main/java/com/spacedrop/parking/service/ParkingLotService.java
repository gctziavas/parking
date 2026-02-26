package com.spacedrop.parking.service;

import com.spacedrop.parking.config.KafkaConfig;
import com.spacedrop.parking.event.ParkingEvent;
import com.spacedrop.parking.model.ParkingLot;
import com.spacedrop.parking.repository.ParkingLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);

    private final ParkingLotRepository parkingLotRepository;
    private final KafkaTemplate<String, ParkingEvent> kafkaTemplate;

    public ParkingLotService(ParkingLotRepository parkingLotRepository,
                             KafkaTemplate<String, ParkingEvent> kafkaTemplate) {
        this.parkingLotRepository = parkingLotRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ParkingLot createParkingLot(ParkingLot parkingLot) {
        parkingLot.setAvailableSpots(parkingLot.getTotalSpots());
        ParkingLot saved = parkingLotRepository.save(parkingLot);
        ParkingEvent event = new ParkingEvent("PARKING_LOT_CREATED", saved.getId(), saved.getAvailableSpots());
        kafkaTemplate.send(KafkaConfig.PARKING_EVENTS_TOPIC, saved.getId().toString(), event);
        log.info("Parking lot created: {}", saved.getName());
        return saved;
    }

    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }

    public List<ParkingLot> getActiveParkingLots() {
        return parkingLotRepository.findByActiveTrue();
    }

    public List<ParkingLot> getAvailableParkingLots() {
        return parkingLotRepository.findByAvailableSpotsGreaterThan(0);
    }

    public Optional<ParkingLot> getParkingLotById(Long id) {
        return parkingLotRepository.findById(id);
    }

    public List<ParkingLot> getParkingLotsByOwner(Long ownerId) {
        return parkingLotRepository.findByOwnerId(ownerId);
    }

    public ParkingLot updateParkingLot(Long id, ParkingLot updated) {
        return parkingLotRepository.findById(id).map(lot -> {
            lot.setName(updated.getName());
            lot.setCountry(updated.getCountry());
            lot.setState(updated.getState());
            lot.setCity(updated.getCity());
            lot.setZipCode(updated.getZipCode());
            lot.setStreetName(updated.getStreetName());
            lot.setStreetNumber(updated.getStreetNumber());
            lot.setLatitude(updated.getLatitude());
            lot.setLongitude(updated.getLongitude());
            lot.setTotalSpots(updated.getTotalSpots());
            lot.setHourlyRate(updated.getHourlyRate());
            lot.setActive(updated.isActive());
            return parkingLotRepository.save(lot);
        }).orElseThrow(() -> new RuntimeException("Parking lot not found: " + id));
    }

    public void deleteParkingLot(Long id) {
        parkingLotRepository.deleteById(id);
        log.info("Parking lot deleted: {}", id);
    }
}
