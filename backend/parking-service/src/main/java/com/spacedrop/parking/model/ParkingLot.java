package com.spacedrop.parking.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_lots")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String country;

    @NotBlank
    @Column(nullable = false)
    private String state;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false)
    private String zipCode;

    @NotBlank
    @Column(nullable = false)
    private String streetName;

    @Column
    private String streetNumber;

    @NotNull
    @Column(nullable = false)
    private Double latitude;

    @NotNull
    @Column(nullable = false)
    private Double longitude;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Integer totalSpots;

    @NotNull
    @Column(nullable = false)
    private Integer availableSpots;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Double hourlyRate;

    @NotNull
    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    private List<PricingTier> pricingTiers = new ArrayList<>();

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dayOfWeek ASC")
    private List<OperatingHours> operatingHours = new ArrayList<>();

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CarCleaningService> carCleaningServices = new ArrayList<>();

    public ParkingLot() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (streetNumber != null) {
            sb.append(streetNumber).append(" ");
        }
        sb.append(streetName).append(", ");
        sb.append(city).append(", ");
        sb.append(state).append(" ");
        sb.append(zipCode).append(", ");
        sb.append(country);
        return sb.toString();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(Integer totalSpots) {
        this.totalSpots = totalSpots;
    }

    public Integer getAvailableSpots() {
        return availableSpots;
    }

    public void setAvailableSpots(Integer availableSpots) {
        this.availableSpots = availableSpots;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<PricingTier> getPricingTiers() {
        return pricingTiers;
    }

    public void setPricingTiers(List<PricingTier> pricingTiers) {
        this.pricingTiers = pricingTiers;
    }

    public void addPricingTier(PricingTier pricingTier) {
        pricingTiers.add(pricingTier);
        pricingTier.setParkingLot(this);
    }

    public void removePricingTier(PricingTier pricingTier) {
        pricingTiers.remove(pricingTier);
        pricingTier.setParkingLot(null);
    }

    public List<OperatingHours> getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(List<OperatingHours> operatingHours) {
        this.operatingHours = operatingHours;
    }

    public void addOperatingHours(OperatingHours hours) {
        operatingHours.add(hours);
        hours.setParkingLot(this);
    }

    public void removeOperatingHours(OperatingHours hours) {
        operatingHours.remove(hours);
        hours.setParkingLot(null);
    }

    public List<CarCleaningService> getCarCleaningServices() {
        return carCleaningServices;
    }

    public void setCarCleaningServices(List<CarCleaningService> carCleaningServices) {
        this.carCleaningServices = carCleaningServices;
    }

    public void addCarCleaningService(CarCleaningService service) {
        carCleaningServices.add(service);
        service.setParkingLot(this);
    }

    public void removeCarCleaningService(CarCleaningService service) {
        carCleaningServices.remove(service);
        service.setParkingLot(null);
    }
}
