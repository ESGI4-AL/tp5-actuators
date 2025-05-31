package fr.esgi.rent.service;

import fr.esgi.rent.client.VelibStationClient;
import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.request.RentalPropertyFilterDto;
import fr.esgi.rent.dto.velib.VelibStationResponseDto;
import fr.esgi.rent.repository.RentalPropertyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static fr.esgi.rent.samples.RentalPropertyEntitySample.*;
import static fr.esgi.rent.samples.RentalPropertyFilterDtoSample.*;
import static fr.esgi.rent.samples.VelibStationDtoSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalPropertyServiceTest {

    @Mock
    private RentalPropertyRepository rentalPropertyRepository;

    @Mock
    private VelibStationClient velibStationClient;

    @InjectMocks
    private RentalPropertyService rentalPropertyService;

    @Test
    void shouldFindAllProperties_WhenFilterIsNull() {
        List<RentalPropertyEntity> expectedProperties = rentalPropertyList();
        when(rentalPropertyRepository.findAll()).thenReturn(expectedProperties);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(null);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedProperties, result);
        verify(rentalPropertyRepository).findAll();
        verifyNoInteractions(velibStationClient);
    }

    @Test
    void shouldFindAllProperties_WhenFilterVelibIsFalse() {
        RentalPropertyFilterDto filter = withoutVelibStations();
        List<RentalPropertyEntity> expectedProperties = rentalPropertyList();
        when(rentalPropertyRepository.findAll()).thenReturn(expectedProperties);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedProperties, result);
        verify(rentalPropertyRepository).findAll();
        verifyNoInteractions(velibStationClient);
    }

    @Test
    void shouldFilterPropertiesNearVelibStations() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();
        VelibStationResponseDto velibResponse = velibStationResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(velibResponse);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertEquals(2, result.size());

        List<String> resultTowns = result.stream()
                .map(RentalPropertyEntity::getTown)
                .toList();
        assertTrue(resultTowns.contains("Paris"));
        assertTrue(resultTowns.contains("Lyon"));
        assertFalse(resultTowns.contains("Marseille"));

        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(Arrays.asList("Paris", "Lyon", "Marseille"));
    }

    @Test
    void shouldReturnEmptyList_WhenVelibResponseIsNull() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(null);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(anyList());
    }

    @Test
    void shouldReturnEmptyList_WhenVelibResponseHasNullStations() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();
        VelibStationResponseDto emptyResponse = nullStationsResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(emptyResponse);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(anyList());
    }

    @Test
    void shouldReturnEmptyList_WhenVelibResponseHasEmptyStations() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();
        VelibStationResponseDto emptyResponse = emptyVelibResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(emptyResponse);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(anyList());
    }

    @Test
    void shouldReturnAllProperties_WhenVelibClientThrowsException() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList()))
                .thenThrow(new RuntimeException("External API error"));

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(allProperties, result);
        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(anyList());
    }

    @Test
    void shouldCallVelibClientWithCorrectTowns() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();
        VelibStationResponseDto velibResponse = velibStationResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(velibResponse);

        rentalPropertyService.findRentalProperties(filter);

        verify(velibStationClient).getStationsByTowns(Arrays.asList("Paris", "Lyon", "Marseille"));
    }

    @Test
    void shouldFilterCorrectly_WhenOnlyOneStationExists() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> allProperties = rentalPropertyList();
        VelibStationResponseDto parisOnlyResponse = parisOnlyResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(allProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(parisOnlyResponse);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Paris", result.get(0).getTown());

        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(anyList());
    }

    @Test
    void shouldHandleEmptyPropertiesList() {
        RentalPropertyFilterDto filter = withVelibStations();
        List<RentalPropertyEntity> emptyProperties = emptyList();
        VelibStationResponseDto velibResponse = emptyVelibResponse();

        when(rentalPropertyRepository.findAll()).thenReturn(emptyProperties);
        when(velibStationClient.getStationsByTowns(anyList())).thenReturn(velibResponse);

        List<RentalPropertyEntity> result = rentalPropertyService.findRentalProperties(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rentalPropertyRepository).findAll();
        verify(velibStationClient).getStationsByTowns(Arrays.asList());
    }
}
