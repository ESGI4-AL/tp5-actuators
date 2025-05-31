package fr.esgi.rent.mapper;

import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.repository.EnergyClassificationRepository;
import fr.esgi.rent.repository.PropertyTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static fr.esgi.rent.samples.EnergyClassificationEntitySample.*;
import static fr.esgi.rent.samples.PropertyTypeEntitySample.*;
import static fr.esgi.rent.samples.RentalPropertyDtoSample.*;
import static fr.esgi.rent.samples.RentalPropertyEntitySample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalPropertyMapperTest {

    @Mock
    private PropertyTypeRepository propertyTypeRepository;

    @Mock
    private EnergyClassificationRepository energyClassificationRepository;

    @InjectMocks
    private RentalPropertyMapper rentalPropertyMapper;

    @Test
    void shouldMapToDtoList() {
        List<RentalPropertyEntity> entities = rentalPropertyList();

        List<RentalPropertyResponseDto> result = rentalPropertyMapper.mapToDtoList(entities);

        assertNotNull(result);
        assertEquals(3, result.size());

        RentalPropertyResponseDto firstDto = result.get(0);
        RentalPropertyEntity firstEntity = entities.get(0);
        assertEquals(firstEntity.getId(), firstDto.id());
        assertEquals(firstEntity.getDescription(), firstDto.description());
        assertEquals(firstEntity.getTown(), firstDto.town());
        assertEquals(firstEntity.getAddress(), firstDto.address());
        assertEquals(firstEntity.getRentAmount(), firstDto.rentAmount());
    }

    @Test
    void shouldMapToDtoList_WithEmptyList() {
        List<RentalPropertyEntity> entities = emptyList();

        List<RentalPropertyResponseDto> result = rentalPropertyMapper.mapToDtoList(entities);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldMapToDto() {
        RentalPropertyEntity entity = rentalPropertyEntity();

        RentalPropertyResponseDto result = rentalPropertyMapper.mapToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getId(), result.id());
        assertEquals(entity.getDescription(), result.description());
        assertEquals(entity.getTown(), result.town());
        assertEquals(entity.getAddress(), result.address());
        assertEquals(entity.getRentAmount(), result.rentAmount());
        assertEquals(entity.getSecurityDepositAmount(), result.securityDepositAmount());
        assertEquals(entity.getArea(), result.area());
        assertEquals(entity.getNumberOfBedrooms(), result.bedroomsCount());
        assertEquals(entity.getFloorNumber().intValue(), result.floorNumber());
        assertEquals(entity.getNumberOfFloors().intValue(), result.numberOfFloors());
        assertEquals(entity.getConstructionYear(), result.constructionYear());
        assertEquals(entity.isHasElevator(), result.hasElevator());
        assertEquals(entity.isHasIntercom(), result.hasIntercom());
        assertEquals(entity.isHasBalcony(), result.hasBalcony());
        assertEquals(entity.isHasParkingSpace(), result.hasParkingSpace());

        assertNotNull(result.propertyType());
        assertEquals(entity.getPropertyType().getId(), result.propertyType().id());
        assertEquals(entity.getPropertyType().getDesignation(), result.propertyType().designation());

        assertNotNull(result.energyClassification());
        assertEquals(entity.getEnergyClassification().getId(), result.energyClassification().id());
        assertEquals(entity.getEnergyClassification().getDesignation(), result.energyClassification().designation());
    }

    @Test
    void shouldMapToDto_WithNullFloorNumbers() {
        RentalPropertyEntity entity = entityWithNullFloors();

        RentalPropertyResponseDto result = rentalPropertyMapper.mapToDto(entity);

        assertEquals(0, result.floorNumber());
        assertEquals(0, result.numberOfFloors());
    }

    @Test
    void shouldMapToEntity() {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        when(propertyTypeRepository.findByDesignation("Apartment"))
                .thenReturn(Optional.of(apartmentType()));
        when(energyClassificationRepository.findByDesignation("A"))
                .thenReturn(Optional.of(classificationA()));

        RentalPropertyEntity result = rentalPropertyMapper.mapToEntity(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.description(), result.getDescription());
        assertEquals(requestDto.town(), result.getTown());
        assertEquals(requestDto.address(), result.getAddress());
        assertEquals(requestDto.rentAmount(), result.getRentAmount());
        assertEquals(requestDto.securityDepositAmount(), result.getSecurityDepositAmount());
        assertEquals(requestDto.area(), result.getArea());
        assertEquals((byte) requestDto.bedroomsCount(), result.getNumberOfBedrooms());
        assertEquals((short) requestDto.floorNumber(), result.getFloorNumber());
        assertEquals((short) requestDto.numberOfFloors(), result.getNumberOfFloors());
        assertEquals((short) requestDto.constructionYear(), result.getConstructionYear());
        assertEquals(requestDto.hasElevator(), result.isHasElevator());
        assertEquals(requestDto.hasIntercom(), result.isHasIntercom());
        assertEquals(requestDto.hasBalcony(), result.isHasBalcony());
        assertEquals(requestDto.hasParkingSpace(), result.isHasParkingSpace());
        assertEquals(apartmentType().getId(), result.getPropertyType().getId());
        assertEquals(classificationA().getId(), result.getEnergyClassification().getId());

        verify(propertyTypeRepository).findByDesignation("Apartment");
        verify(energyClassificationRepository).findByDesignation("A");
    }

    @Test
    void shouldMapToEntity_WithStudioType() {
        RentalPropertyRequestDto requestDto = studioRequestDto();
        when(propertyTypeRepository.findByDesignation("Studio"))
                .thenReturn(Optional.of(studioType()));
        when(energyClassificationRepository.findByDesignation("B"))
                .thenReturn(Optional.of(classificationB()));

        RentalPropertyEntity result = rentalPropertyMapper.mapToEntity(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.description(), result.getDescription());
        assertEquals(requestDto.town(), result.getTown());
        assertEquals(studioType().getId(), result.getPropertyType().getId());
        assertEquals(classificationB().getId(), result.getEnergyClassification().getId());

        verify(propertyTypeRepository).findByDesignation("Studio");
        verify(energyClassificationRepository).findByDesignation("B");
    }

    @Test
    void shouldThrowException_WhenPropertyTypeNotFound() {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        when(propertyTypeRepository.findByDesignation(anyString()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalPropertyMapper.mapToEntity(requestDto)
        );

        assertTrue(exception.getMessage().contains("Property type not found"));
        verify(propertyTypeRepository).findByDesignation("Apartment");
    }

    @Test
    void shouldThrowException_WhenEnergyClassificationNotFound() {
        RentalPropertyRequestDto requestDto = rentalPropertyRequestDto();
        when(propertyTypeRepository.findByDesignation("Apartment"))
                .thenReturn(Optional.of(apartmentType()));
        when(energyClassificationRepository.findByDesignation(anyString()))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> rentalPropertyMapper.mapToEntity(requestDto)
        );

        assertTrue(exception.getMessage().contains("Energy classification not found"));
        verify(propertyTypeRepository).findByDesignation("Apartment");
        verify(energyClassificationRepository).findByDesignation("A");
    }
}
