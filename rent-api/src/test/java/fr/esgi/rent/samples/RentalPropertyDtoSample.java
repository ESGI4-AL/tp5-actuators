package fr.esgi.rent.samples;

import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RentalPropertyDtoSample {

    public static RentalPropertyRequestDto rentalPropertyRequestDto() {
        return RentalPropertyRequestDto.builder()
                .description("Beautiful apartment in city center")
                .town("Paris")
                .address("123 Rue de la Paix")
                .propertyType("Apartment")
                .rentAmount(1200.0)
                .securityDepositAmount(2400.0)
                .area(65.0)
                .bedroomsCount(2)
                .floorNumber(4)
                .numberOfFloors(6)
                .constructionYear(2015)
                .energyClassification("A")
                .hasElevator(true)
                .hasIntercom(true)
                .hasBalcony(false)
                .hasParkingSpace(true)
                .build();
    }

    public static RentalPropertyRequestDto studioRequestDto() {
        return RentalPropertyRequestDto.builder()
                .description("Cozy studio in downtown")
                .town("Lyon")
                .address("456 Avenue des Tests")
                .propertyType("Studio")
                .rentAmount(800.0)
                .securityDepositAmount(1600.0)
                .area(35.0)
                .bedroomsCount(1)
                .floorNumber(2)
                .numberOfFloors(4)
                .constructionYear(2010)
                .energyClassification("B")
                .hasElevator(false)
                .hasIntercom(false)
                .hasBalcony(true)
                .hasParkingSpace(false)
                .build();
    }

    public static RentalPropertyRequestDto invalidRequestDto() {
        return RentalPropertyRequestDto.builder()
                .description(null)
                .town("Paris")
                .address("123 Test Street")
                .propertyType("Apartment")
                .rentAmount(-100.0)
                .securityDepositAmount(2000.0)
                .area(50.0)
                .bedroomsCount(0)
                .floorNumber(3)
                .numberOfFloors(5)
                .constructionYear(2020)
                .energyClassification("A")
                .hasElevator(true)
                .hasIntercom(true)
                .hasBalcony(true)
                .hasParkingSpace(true)
                .build();
    }

    public static RentalPropertyResponseDto rentalPropertyResponseDto() {
        return RentalPropertyResponseDto.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                .description("Beautiful apartment in city center")
                .town("Paris")
                .address("123 Rue de la Paix")
                .propertyType(new RentalPropertyResponseDto.PropertyTypeDto(
                        UUID.fromString("a50e8400-e29b-41d4-a716-446655440001"),
                        "Apartment"
                ))
                .rentAmount(1200.0)
                .securityDepositAmount(2400.0)
                .area(65.0)
                .bedroomsCount(2)
                .floorNumber(4)
                .numberOfFloors(6)
                .constructionYear(2015)
                .energyClassification(new RentalPropertyResponseDto.EnergyClassificationDto(
                        UUID.fromString("b50e8400-e29b-41d4-a716-446655440001"),
                        "A"
                ))
                .hasElevator(true)
                .hasIntercom(true)
                .hasBalcony(false)
                .hasParkingSpace(true)
                .build();
    }

    public static RentalPropertyResponseDto studioResponseDto() {
        return RentalPropertyResponseDto.builder()
                .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                .description("Cozy studio in downtown")
                .town("Lyon")
                .address("456 Avenue des Tests")
                .propertyType(new RentalPropertyResponseDto.PropertyTypeDto(
                        UUID.fromString("a50e8400-e29b-41d4-a716-446655440002"),
                        "Studio"
                ))
                .rentAmount(800.0)
                .securityDepositAmount(1600.0)
                .area(35.0)
                .bedroomsCount(1)
                .floorNumber(2)
                .numberOfFloors(4)
                .constructionYear(2010)
                .energyClassification(new RentalPropertyResponseDto.EnergyClassificationDto(
                        UUID.fromString("b50e8400-e29b-41d4-a716-446655440002"),
                        "B"
                ))
                .hasElevator(false)
                .hasIntercom(false)
                .hasBalcony(true)
                .hasParkingSpace(false)
                .build();
    }

    public static List<RentalPropertyResponseDto> rentalPropertyResponseDtoList() {
        return Arrays.asList(
                rentalPropertyResponseDto(),
                studioResponseDto()
        );
    }
}
