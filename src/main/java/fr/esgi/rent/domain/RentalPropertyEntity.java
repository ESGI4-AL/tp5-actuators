package fr.esgi.rent.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rental_property")
public class RentalPropertyEntity {
    @GeneratedValue
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "town")
    private String town;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "property_type_id")
    private PropertyTypeEntity propertyType;

    @Column(name = "rent_amount")
    private double rentAmount;

    @Column(name = "security_deposit_amount")
    private double securityDepositAmount;

    @Column(name = "area")
    private double area;

    @Column(name = "number_of_bedrooms")
    private byte numberOfBedrooms;

    @Column(name = "floor_number")
    private Short floorNumber;

    @Column(name = "number_of_floors")
    private Short numberOfFloors;

    @Column(name = "construction_year")
    private short constructionYear;

    @ManyToOne
    @JoinColumn(name = "energy_classification_id")
    private EnergyClassificationEntity energyClassification;

    @Column(name = "has_elevator")
    private boolean hasElevator;

    @Column(name = "has_intercom")
    private boolean hasIntercom;

    @Column(name = "has_balcony")
    private boolean hasBalcony;

    @Column(name = "has_parking_space")
    private boolean hasParkingSpace;

    public RentalPropertyEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PropertyTypeEntity getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyTypeEntity propertyType) {
        this.propertyType = propertyType;
    }

    public double getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public double getSecurityDepositAmount() {
        return securityDepositAmount;
    }

    public void setSecurityDepositAmount(double securityDepositAmount) {
        this.securityDepositAmount = securityDepositAmount;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public byte getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(byte numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public Short getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Short floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Short getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(Short numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public short getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(short constructionYear) {
        this.constructionYear = constructionYear;
    }

    public EnergyClassificationEntity getEnergyClassification() {
        return energyClassification;
    }

    public void setEnergyClassification(EnergyClassificationEntity energyClassification) {
        this.energyClassification = energyClassification;
    }

    public boolean isHasElevator() {
        return hasElevator;
    }

    public void setHasElevator(boolean hasElevator) {
        this.hasElevator = hasElevator;
    }

    public boolean isHasIntercom() {
        return hasIntercom;
    }

    public void setHasIntercom(boolean hasIntercom) {
        this.hasIntercom = hasIntercom;
    }

    public boolean isHasBalcony() {
        return hasBalcony;
    }

    public void setHasBalcony(boolean hasBalcony) {
        this.hasBalcony = hasBalcony;
    }

    public boolean isHasParkingSpace() {
        return hasParkingSpace;
    }

    public void setHasParkingSpace(boolean hasParkingSpace) {
        this.hasParkingSpace = hasParkingSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalPropertyEntity that = (RentalPropertyEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
