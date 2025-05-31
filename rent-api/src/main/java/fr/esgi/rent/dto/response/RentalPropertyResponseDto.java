package fr.esgi.rent.dto.response;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Builder
public record RentalPropertyResponseDto(
        @NotNull UUID id,
        @NotNull String description,
        @NotNull String town,
        @NotNull String address,
        @NotNull PropertyTypeDto propertyType,
        @Min(1) double rentAmount,
        @Min(1) double securityDepositAmount,
        @Min(1) double area,
        @Min(1) int bedroomsCount,
        @NotNull int floorNumber,
        int numberOfFloors,
        int constructionYear,
        @NotNull EnergyClassificationDto energyClassification,
        @NotNull boolean hasElevator,
        @NotNull boolean hasIntercom,
        @NotNull boolean hasBalcony,
        @NotNull boolean hasParkingSpace) {

    public record PropertyTypeDto(
            UUID id,
            String designation
    ) {}

    public record EnergyClassificationDto(
            UUID id,
            String designation
    ) {}
}
