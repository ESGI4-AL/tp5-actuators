package service;

import fr.esgi.velib.api.VelibStationController.StationStats;
import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import fr.esgi.velib.mapper.VelibStationMapper;
import fr.esgi.velib.repository.VelibStationRepository;
import fr.esgi.velib.service.VelibStationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static samples.VelibStationDtoSample.*;
import static samples.VelibStationEntitySample.*;

@ExtendWith(MockitoExtension.class)
class VelibStationServiceTest {

    @Mock
    private VelibStationRepository velibStationRepository;

    @Mock
    private VelibStationMapper velibStationMapper;

    @InjectMocks
    private VelibStationService velibStationService;

    @Test
    void shouldGetStationsByTowns() {
        List<String> towns = Arrays.asList("Paris", "Lyon");
        List<VelibStationEntity> entities = Arrays.asList(parisStation(), lyonStation());
        List<VelibStationResponseDto.StationDto> stationDtos = Arrays.asList(parisStationDto(), lyonStationDto());

        when(velibStationRepository.findByNomCommuneIn(towns)).thenReturn(entities);
        when(velibStationMapper.mapToDtoList(entities)).thenReturn(stationDtos);

        VelibStationResponseDto result = velibStationService.getStationsByTowns(towns);

        assertNotNull(result);
        assertEquals(2, result.stations().size());
        assertEquals("République", result.stations().get(0).name());
        assertEquals("Bellecour", result.stations().get(1).name());

        verify(velibStationRepository).findByNomCommuneIn(towns);
        verify(velibStationMapper).mapToDtoList(entities);
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    @Test
    void shouldGetAllStations() {
        List<VelibStationEntity> allEntities = allStations();
        List<VelibStationResponseDto.StationDto> allDtos = Arrays.asList(
                parisStationDto(), lyonStationDto(), marseilleStationDto(), emptyStationDto()
        );

        when(velibStationRepository.findAll()).thenReturn(allEntities);
        when(velibStationMapper.mapToDtoList(allEntities)).thenReturn(allDtos);

        VelibStationResponseDto result = velibStationService.getAllStations();

        assertNotNull(result);
        assertEquals(4, result.stations().size());

        verify(velibStationRepository).findAll();
        verify(velibStationMapper).mapToDtoList(allEntities);
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    @Test
    void shouldGetStationsByTown() {
        String town = "Paris";
        List<VelibStationEntity> parisEntities = parisStations();
        List<VelibStationResponseDto.StationDto> parisDtos = Arrays.asList(parisStationDto(), emptyStationDto());

        when(velibStationRepository.findByNomCommune(town)).thenReturn(parisEntities);
        when(velibStationMapper.mapToDtoList(parisEntities)).thenReturn(parisDtos);

        VelibStationResponseDto result = velibStationService.getStationsByTown(town);

        assertNotNull(result);
        assertEquals(2, result.stations().size());
        assertTrue(result.stations().stream().allMatch(station -> "Paris".equals(station.nomCommune())));

        verify(velibStationRepository).findByNomCommune(town);
        verify(velibStationMapper).mapToDtoList(parisEntities);
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }



    @Test
    void shouldGetAvailableCommunes() {
        List<String> expectedCommunes = Arrays.asList("Lyon", "Marseille", "Paris");
        when(velibStationRepository.findAllCommunes()).thenReturn(expectedCommunes);

        List<String> result = velibStationService.getAvailableCommunes();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedCommunes, result);

        verify(velibStationRepository).findAllCommunes();
        verifyNoMoreInteractions(velibStationRepository);
        verifyNoInteractions(velibStationMapper);
    }

    @Test
    void shouldGetStationStats() {
        List<VelibStationEntity> allEntities = Arrays.asList(
                parisStation(),
                lyonStation(),
                marseilleStation(),
                emptyStation(),
                maintenanceStation()
        );

        when(velibStationRepository.findAll()).thenReturn(allEntities);

        StationStats result = velibStationService.getStationStats();

        assertNotNull(result);
        assertEquals(5L, result.totalStations());
        assertEquals(3L, result.totalCommunes());
        assertEquals(3L, result.availableStations());
        assertEquals(52L, result.totalBikes());
        assertEquals(43L, result.totalDocks());

        verify(velibStationRepository).findAll();
        verifyNoMoreInteractions(velibStationRepository);
        verifyNoInteractions(velibStationMapper);
    }

    @Test
    void shouldHandleEmptyStationsList() {
        List<VelibStationEntity> emptyEntities = emptyList();
        List<VelibStationResponseDto.StationDto> emptyDtos = Arrays.asList();

        when(velibStationRepository.findAll()).thenReturn(emptyEntities);
        when(velibStationMapper.mapToDtoList(emptyEntities)).thenReturn(emptyDtos);

        VelibStationResponseDto result = velibStationService.getAllStations();

        assertNotNull(result);
        assertTrue(result.stations().isEmpty());

        verify(velibStationRepository).findAll();
        verify(velibStationMapper).mapToDtoList(emptyEntities);
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    @Test
    void shouldHandleUnknownTown() {
        String unknownTown = "UnknownCity";
        List<VelibStationEntity> emptyEntities = emptyList();
        List<VelibStationResponseDto.StationDto> emptyDtos = Arrays.asList();

        when(velibStationRepository.findByNomCommune(unknownTown)).thenReturn(emptyEntities);
        when(velibStationMapper.mapToDtoList(emptyEntities)).thenReturn(emptyDtos);

        VelibStationResponseDto result = velibStationService.getStationsByTown(unknownTown);

        assertNotNull(result);
        assertTrue(result.stations().isEmpty());

        verify(velibStationRepository).findByNomCommune(unknownTown);
        verify(velibStationMapper).mapToDtoList(emptyEntities);
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }



    @Test
    void shouldReturnEmptyStatsForEmptyRepository() {
        List<VelibStationEntity> emptyEntities = emptyList();
        when(velibStationRepository.findAll()).thenReturn(emptyEntities);

        StationStats result = velibStationService.getStationStats();

        assertNotNull(result);
        assertEquals(0L, result.totalStations());
        assertEquals(0L, result.totalCommunes());
        assertEquals(0L, result.availableStations());
        assertEquals(0L, result.totalBikes());
        assertEquals(0L, result.totalDocks());

        verify(velibStationRepository).findAll();
        verifyNoMoreInteractions(velibStationRepository);
        verifyNoInteractions(velibStationMapper);
    }

    @Test
    void shouldGetAvailableStationsByTowns() {
        List<String> towns = Arrays.asList("Paris", "Lyon");
        List<VelibStationEntity> allEntities = Arrays.asList(
                parisStation(),
                lyonStation(),
                emptyStation(),
                maintenanceStation()
        );

        List<VelibStationResponseDto.StationDto> availableDtos = Arrays.asList(parisStationDto(), lyonStationDto());

        when(velibStationRepository.findByNomCommuneIn(towns)).thenReturn(allEntities);
        when(velibStationMapper.mapToDtoList(any())).thenReturn(availableDtos);

        VelibStationResponseDto result = velibStationService.getAvailableStationsByTowns(towns);

        assertNotNull(result);
        assertEquals(2, result.stations().size());
        assertEquals("République", result.stations().get(0).name());
        assertEquals("Bellecour", result.stations().get(1).name());

        verify(velibStationRepository).findByNomCommuneIn(towns);
        verify(velibStationMapper).mapToDtoList(any());
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableStations() {
        List<String> towns = Arrays.asList("Paris");
        List<VelibStationEntity> onlyUnavailableStations = Arrays.asList(
                emptyStation(),
                maintenanceStation()
        );

        List<VelibStationResponseDto.StationDto> emptyDtos = Arrays.asList();

        when(velibStationRepository.findByNomCommuneIn(towns)).thenReturn(onlyUnavailableStations);
        when(velibStationMapper.mapToDtoList(any())).thenReturn(emptyDtos);

        VelibStationResponseDto result = velibStationService.getAvailableStationsByTowns(towns);

        assertNotNull(result);
        assertTrue(result.stations().isEmpty());

        verify(velibStationRepository).findByNomCommuneIn(towns);
        verify(velibStationMapper).mapToDtoList(any());
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    @Test
    void shouldFilterStationsWithBikesButNotRenting() {
        List<String> towns = Arrays.asList("Paris");

        List<VelibStationEntity> allEntities = getVelibStationEntities();

        List<VelibStationResponseDto.StationDto> availableDtos = Arrays.asList(parisStationDto());

        when(velibStationRepository.findByNomCommuneIn(towns)).thenReturn(allEntities);
        when(velibStationMapper.mapToDtoList(any())).thenReturn(availableDtos);

        VelibStationResponseDto result = velibStationService.getAvailableStationsByTowns(towns);

        assertNotNull(result);
        assertEquals(1, result.stations().size());
        assertEquals("République", result.stations().get(0).name());

        verify(velibStationRepository).findByNomCommuneIn(towns);
        verify(velibStationMapper).mapToDtoList(any());
        verifyNoMoreInteractions(velibStationRepository, velibStationMapper);
    }

    private static List<VelibStationEntity> getVelibStationEntities() {
        VelibStationEntity stationNotRenting = new VelibStationEntity(
                "75003-01", "Station Non Location", "OUI", 30, 10, 15, 10, 5,
                "NON", "OUI", "2024-12-31T23:59:59+00:00",
                2.3522, 48.8566, "Paris", "75003", "24/7"
        );

        return Arrays.asList(
                parisStation(),
                stationNotRenting
        );
    }
}
