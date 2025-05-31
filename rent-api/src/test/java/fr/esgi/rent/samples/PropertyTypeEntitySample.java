package fr.esgi.rent.samples;

import fr.esgi.rent.domain.PropertyTypeEntity;

import java.util.UUID;

public class PropertyTypeEntitySample {

    public static PropertyTypeEntity apartmentType() {
        PropertyTypeEntity propertyType = new PropertyTypeEntity();
        propertyType.setId(UUID.fromString("a50e8400-e29b-41d4-a716-446655440001"));
        propertyType.setDesignation("Apartment");
        return propertyType;
    }

    public static PropertyTypeEntity studioType() {
        PropertyTypeEntity propertyType = new PropertyTypeEntity();
        propertyType.setId(UUID.fromString("a50e8400-e29b-41d4-a716-446655440002"));
        propertyType.setDesignation("Studio");
        return propertyType;
    }

    public static PropertyTypeEntity houseType() {
        PropertyTypeEntity propertyType = new PropertyTypeEntity();
        propertyType.setId(UUID.fromString("a50e8400-e29b-41d4-a716-446655440003"));
        propertyType.setDesignation("House");
        return propertyType;
    }
}
