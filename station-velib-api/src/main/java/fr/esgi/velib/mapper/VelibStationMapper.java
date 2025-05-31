package fr.esgi.velib.mapper;

import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VelibStationMapper {

    public List<VelibStationResponseDto.StationDto> mapToDtoList(List<VelibStationEntity> entities) {
        return entities.stream()
                .map(this::mapToDto)
                .toList();
    }

    public VelibStationResponseDto.StationDto mapToDto(VelibStationEntity entity) {
        return new VelibStationResponseDto.StationDto(
                entity.getStationCode(),
                entity.getName(),
                entity.getIsInstalled(),
                entity.getCapacity(),
                entity.getDocksAvailable(),
                entity.getBikesAvailable(),
                entity.getMechanical(),
                entity.geteBike(),
                entity.getIsRenting(),
                entity.getIsReturning(),
                entity.getDueDate(),
                new VelibStationResponseDto.CoordinatesDto(
                        entity.getLongitude(),
                        entity.getLatitude()
                ),
                entity.getNomCommune(),
                entity.getCodeInseeCommune(),
                entity.getStationOpeningHours()
        );
    }

    public VelibStationEntity mapToEntity(VelibStationResponseDto.StationDto dto) {
        return new VelibStationEntity(
                dto.stationCode(),
                dto.name(),
                dto.isInstalled(),
                dto.capacity(),
                dto.docksAvailable(),
                dto.bikesAvailable(),
                dto.mechanical(),
                dto.eBike(),
                dto.isRenting(),
                dto.isReturning(),
                dto.dueDate(),
                dto.coordonneesGeo().lon(),
                dto.coordonneesGeo().lat(),
                dto.nomCommune(),
                dto.codeInseeCommune(),
                dto.stationOpeningHours()
        );
    }
}
