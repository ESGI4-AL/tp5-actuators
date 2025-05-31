package fr.esgi.rent.samples;

import fr.esgi.rent.domain.RentalPropertyEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RentalPropertyEntitySample {

    public static RentalPropertyEntity rentalPropertyEntity() {
        RentalPropertyEntity entity = new RentalPropertyEntity();
        entity.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        entity.setDescription("Beautiful apartment in city center");
        entity.setTown("Paris");
        entity.setAddress("123 Rue de la Paix");
        entity.setPropertyType(PropertyTypeEntitySample.apartmentType());
        entity.setRentAmount(1200.0);
        entity.setSecurityDepositAmount(2400.0);
        entity.setArea(65.0);
        entity.setNumberOfBedrooms((byte) 2);
        entity.setFloorNumber((short) 4);
        entity.setNumberOfFloors((short) 6);
        entity.setConstructionYear((short) 2015);
        entity.setEnergyClassification(EnergyClassificationEntitySample.classificationA());
        entity.setHasElevator(true);
        entity.setHasIntercom(true);
        entity.setHasBalcony(false);
        entity.setHasParkingSpace(true);
        return entity;
    }

    public static RentalPropertyEntity studioEntity() {
        RentalPropertyEntity entity = new RentalPropertyEntity();
        entity.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"));
        entity.setDescription("Cozy studio in downtown");
        entity.setTown("Lyon");
        entity.setAddress("456 Avenue des Tests");
        entity.setPropertyType(PropertyTypeEntitySample.studioType());
        entity.setRentAmount(800.0);
        entity.setSecurityDepositAmount(1600.0);
        entity.setArea(35.0);
        entity.setNumberOfBedrooms((byte) 1);
        entity.setFloorNumber((short) 2);
        entity.setNumberOfFloors((short) 4);
        entity.setConstructionYear((short) 2010);
        entity.setEnergyClassification(EnergyClassificationEntitySample.classificationB());
        entity.setHasElevator(false);
        entity.setHasIntercom(false);
        entity.setHasBalcony(true);
        entity.setHasParkingSpace(false);
        return entity;
    }

    public static RentalPropertyEntity houseEntity() {
        RentalPropertyEntity entity = new RentalPropertyEntity();
        entity.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"));
        entity.setDescription("Spacious house with garden");
        entity.setTown("Marseille");
        entity.setAddress("789 Boulevard de la Mer");
        entity.setPropertyType(PropertyTypeEntitySample.houseType());
        entity.setRentAmount(1800.0);
        entity.setSecurityDepositAmount(3600.0);
        entity.setArea(120.0);
        entity.setNumberOfBedrooms((byte) 4);
        entity.setFloorNumber((short) 0);
        entity.setNumberOfFloors((short) 2);
        entity.setConstructionYear((short) 2020);
        entity.setEnergyClassification(EnergyClassificationEntitySample.classificationA());
        entity.setHasElevator(false);
        entity.setHasIntercom(true);
        entity.setHasBalcony(false);
        entity.setHasParkingSpace(true);
        return entity;
    }

    public static RentalPropertyEntity entityWithNullFloors() {
        RentalPropertyEntity entity = rentalPropertyEntity();
        entity.setFloorNumber(null);
        entity.setNumberOfFloors(null);
        return entity;
    }

    public static List<RentalPropertyEntity> rentalPropertyList() {
        return Arrays.asList(
                rentalPropertyEntity(),
                studioEntity(),
                houseEntity()
        );
    }

    public static List<RentalPropertyEntity> emptyList() {
        return Arrays.asList();
    }
}
