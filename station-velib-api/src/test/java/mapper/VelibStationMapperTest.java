package mapper;

import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import fr.esgi.velib.mapper.VelibStationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static samples.VelibStationDtoSample.parisStationDto;
import static samples.VelibStationEntitySample.*;

@ExtendWith(MockitoExtension.class)
class VelibStationMapperTest {

    @InjectMocks
    private VelibStationMapper velibStationMapper;

    @Test
    void shouldMapToDtoList() {
        List<VelibStationEntity> entities = allStations();

        List<VelibStationResponseDto.StationDto> result = velibStationMapper.mapToDtoList(entities);

        assertNotNull(result);
        assertEquals(5, result.size());

        VelibStationResponseDto.StationDto firstDto = result.get(0);
        VelibStationEntity firstEntity = entities.get(0);
        assertEquals(firstEntity.getStationCode(), firstDto.stationCode());
        assertEquals(firstEntity.getName(), firstDto.name());
        assertEquals(firstEntity.getNomCommune(), firstDto.nomCommune());
    }

    @Test
    void shouldMapToDtoList_WithEmptyList() {
        List<VelibStationEntity> entities = emptyList();

        List<VelibStationResponseDto.StationDto> result = velibStationMapper.mapToDtoList(entities);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldMapToDto() {
        VelibStationEntity entity = parisStation();

        VelibStationResponseDto.StationDto result = velibStationMapper.mapToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getStationCode(), result.stationCode());
        assertEquals(entity.getName(), result.name());
        assertEquals(entity.getIsInstalled(), result.isInstalled());
        assertEquals(entity.getCapacity(), result.capacity());
        assertEquals(entity.getDocksAvailable(), result.docksAvailable());
        assertEquals(entity.getBikesAvailable(), result.bikesAvailable());
        assertEquals(entity.getMechanical(), result.mechanical());
        assertEquals(entity.geteBike(), result.eBike());
        assertEquals(entity.getIsRenting(), result.isRenting());
        assertEquals(entity.getIsReturning(), result.isReturning());
        assertEquals(entity.getDueDate(), result.dueDate());
        assertEquals(entity.getNomCommune(), result.nomCommune());
        assertEquals(entity.getCodeInseeCommune(), result.codeInseeCommune());
        assertEquals(entity.getStationOpeningHours(), result.stationOpeningHours());

        assertNotNull(result.coordonneesGeo());
        assertEquals(entity.getLongitude(), result.coordonneesGeo().lon());
        assertEquals(entity.getLatitude(), result.coordonneesGeo().lat());
    }

    @Test
    void shouldMapToEntity() {
        VelibStationResponseDto.StationDto dto = parisStationDto();

        VelibStationEntity result = velibStationMapper.mapToEntity(dto);

        assertNotNull(result);
        assertEquals(dto.stationCode(), result.getStationCode());
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.isInstalled(), result.getIsInstalled());
        assertEquals(dto.capacity(), result.getCapacity());
        assertEquals(dto.docksAvailable(), result.getDocksAvailable());
        assertEquals(dto.bikesAvailable(), result.getBikesAvailable());
        assertEquals(dto.mechanical(), result.getMechanical());
        assertEquals(dto.eBike(), result.geteBike());
        assertEquals(dto.isRenting(), result.getIsRenting());
        assertEquals(dto.isReturning(), result.getIsReturning());
        assertEquals(dto.dueDate(), result.getDueDate());
        assertEquals(dto.nomCommune(), result.getNomCommune());
        assertEquals(dto.codeInseeCommune(), result.getCodeInseeCommune());
        assertEquals(dto.stationOpeningHours(), result.getStationOpeningHours());

        assertEquals(dto.coordonneesGeo().lon(), result.getLongitude());
        assertEquals(dto.coordonneesGeo().lat(), result.getLatitude());
    }

    @Test
    void shouldMapLyonStation() {
        VelibStationEntity entity = lyonStation();

        VelibStationResponseDto.StationDto result = velibStationMapper.mapToDto(entity);

        assertNotNull(result);
        assertEquals("69001-01", result.stationCode());
        assertEquals("Bellecour", result.name());
        assertEquals("Lyon", result.nomCommune());
        assertEquals(4.8357, result.coordonneesGeo().lon());
        assertEquals(45.7640, result.coordonneesGeo().lat());
    }

    @Test
    void shouldMapEmptyStation() {
        VelibStationEntity entity = emptyStation();

        VelibStationResponseDto.StationDto result = velibStationMapper.mapToDto(entity);

        assertNotNull(result);
        assertEquals("75002-01", result.stationCode());
        assertEquals("Station Vide", result.name());
        assertEquals(0, result.bikesAvailable());
        assertEquals("NON", result.isRenting());
        assertEquals("Paris", result.nomCommune());
    }

    @Test
    void shouldMapMaintenanceStation() {
        VelibStationEntity entity = maintenanceStation();

        VelibStationResponseDto.StationDto result = velibStationMapper.mapToDto(entity);

        assertNotNull(result);
        assertEquals("69002-01", result.stationCode());
        assertEquals("Station Maintenance", result.name());
        assertEquals("NON", result.isInstalled());
        assertEquals("NON", result.isRenting());
        assertEquals("NON", result.isReturning());
        assertNull(result.stationOpeningHours());
    }

    @Test
    void shouldHandleBidirectionalMapping() {
        VelibStationEntity originalEntity = marseilleStation();

        VelibStationResponseDto.StationDto dto = velibStationMapper.mapToDto(originalEntity);
        VelibStationEntity mappedBackEntity = velibStationMapper.mapToEntity(dto);

        assertEquals(originalEntity.getStationCode(), mappedBackEntity.getStationCode());
        assertEquals(originalEntity.getName(), mappedBackEntity.getName());
        assertEquals(originalEntity.getIsInstalled(), mappedBackEntity.getIsInstalled());
        assertEquals(originalEntity.getCapacity(), mappedBackEntity.getCapacity());
        assertEquals(originalEntity.getDocksAvailable(), mappedBackEntity.getDocksAvailable());
        assertEquals(originalEntity.getBikesAvailable(), mappedBackEntity.getBikesAvailable());
        assertEquals(originalEntity.getMechanical(), mappedBackEntity.getMechanical());
        assertEquals(originalEntity.geteBike(), mappedBackEntity.geteBike());
        assertEquals(originalEntity.getIsRenting(), mappedBackEntity.getIsRenting());
        assertEquals(originalEntity.getIsReturning(), mappedBackEntity.getIsReturning());
        assertEquals(originalEntity.getDueDate(), mappedBackEntity.getDueDate());
        assertEquals(originalEntity.getLongitude(), mappedBackEntity.getLongitude());
        assertEquals(originalEntity.getLatitude(), mappedBackEntity.getLatitude());
        assertEquals(originalEntity.getNomCommune(), mappedBackEntity.getNomCommune());
        assertEquals(originalEntity.getCodeInseeCommune(), mappedBackEntity.getCodeInseeCommune());
        assertEquals(originalEntity.getStationOpeningHours(), mappedBackEntity.getStationOpeningHours());
    }
}
