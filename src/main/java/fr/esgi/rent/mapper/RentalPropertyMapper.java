package fr.esgi.rent.mapper;

import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RentalPropertyMapper {

    public List<RentalPropertyResponseDto> mapToDtoList(List<RentalPropertyEntity> rentalProperties) {
        return rentalProperties.stream()
                .map(this::mapToDto)
                .toList();
    }

    public RentalPropertyResponseDto mapToDto(RentalPropertyEntity rentalProperty) {
        return RentalPropertyResponseDto.builder()
                .id(rentalProperty.getId())
                .description(rentalProperty.getDescription())
                .town(rentalProperty.getTown())
                .address(rentalProperty.getAddress())
                .propertyType(new RentalPropertyResponseDto.PropertyTypeDto(
                        rentalProperty.getPropertyType().getId(),
                        rentalProperty.getPropertyType().getDesignation()
                ))
                .rentAmount(rentalProperty.getRentAmount())
                .securityDepositAmount(rentalProperty.getSecurityDepositAmount())
                .area(rentalProperty.getArea())
                .bedroomsCount(rentalProperty.getNumberOfBedrooms())
                .floorNumber(rentalProperty.getFloorNumber() != null ? rentalProperty.getFloorNumber() : 0)
                .numberOfFloors(rentalProperty.getNumberOfFloors() != null ? rentalProperty.getNumberOfFloors() : 0)
                .constructionYear(rentalProperty.getConstructionYear())
                .energyClassification(new RentalPropertyResponseDto.EnergyClassificationDto(
                        rentalProperty.getEnergyClassification().getId(),
                        rentalProperty.getEnergyClassification().getDesignation()
                ))
                .hasElevator(rentalProperty.isHasElevator())
                .hasIntercom(rentalProperty.isHasIntercom())
                .hasBalcony(rentalProperty.isHasBalcony())
                .hasParkingSpace(rentalProperty.isHasParkingSpace())
                .build();
    }
}
