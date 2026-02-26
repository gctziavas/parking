package com.spacedrop.parking.repository;

import com.spacedrop.parking.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    List<ParkingLot> findByOwnerId(Long ownerId);

    List<ParkingLot> findByActiveTrue();

    List<ParkingLot> findByAvailableSpotsGreaterThan(int minSpots);
}
