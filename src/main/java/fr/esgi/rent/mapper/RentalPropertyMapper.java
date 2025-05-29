package fr.esgi.rent.mapper;

import fr.esgi.rent.domain.EnergyClassificationEntity;
import fr.esgi.rent.domain.PropertyTypeEntity;
import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.repository.EnergyClassificationRepository;
import fr.esgi.rent.repository.PropertyTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RentalPropertyMapper {

    private final PropertyTypeRepository propertyTypeRepository;
    private final EnergyClassificationRepository energyClassificationRepository;

    public RentalPropertyMapper(PropertyTypeRepository propertyTypeRepository,
                                EnergyClassificationRepository energyClassificationRepository) {
        this.propertyTypeRepository = propertyTypeRepository;
        this.energyClassificationRepository = energyClassificationRepository;
    }

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

    public RentalPropertyEntity mapToEntity(RentalPropertyRequestDto requestDto) {
        PropertyTypeEntity propertyType = propertyTypeRepository
                .findByDesignation(requestDto.propertyType())
                .orElseThrow(() -> new IllegalArgumentException("Property type not found: " + requestDto.propertyType()));

        EnergyClassificationEntity energyClassification = energyClassificationRepository
                .findByDesignation(requestDto.energyClassification())
                .orElseThrow(() -> new IllegalArgumentException("Energy classification not found: " + requestDto.energyClassification()));

        RentalPropertyEntity entity = new RentalPropertyEntity();
        entity.setDescription(requestDto.description());
        entity.setTown(requestDto.town());
        entity.setAddress(requestDto.address());
        entity.setPropertyType(propertyType);
        entity.setRentAmount(requestDto.rentAmount());
        entity.setSecurityDepositAmount(requestDto.securityDepositAmount());
        entity.setArea(requestDto.area());
        entity.setNumberOfBedrooms((byte) requestDto.bedroomsCount());
        entity.setFloorNumber((short) requestDto.floorNumber());
        entity.setNumberOfFloors((short) requestDto.numberOfFloors());
        entity.setConstructionYear((short) requestDto.constructionYear());
        entity.setEnergyClassification(energyClassification);
        entity.setHasElevator(requestDto.hasElevator());
        entity.setHasIntercom(requestDto.hasIntercom());
        entity.setHasBalcony(requestDto.hasBalcony());
        entity.setHasParkingSpace(requestDto.hasParkingSpace());

        return entity;
    }
}
