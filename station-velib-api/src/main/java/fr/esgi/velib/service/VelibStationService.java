package fr.esgi.velib.service;

import fr.esgi.velib.api.VelibStationController.StationStats;
import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import fr.esgi.velib.mapper.VelibStationMapper;
import fr.esgi.velib.repository.VelibStationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VelibStationService {

    private final VelibStationRepository velibStationRepository;
    private final VelibStationMapper velibStationMapper;

    public VelibStationService(VelibStationRepository velibStationRepository,
                               VelibStationMapper velibStationMapper) {
        this.velibStationRepository = velibStationRepository;
        this.velibStationMapper = velibStationMapper;
    }

    public VelibStationResponseDto getStationsByTowns(List<String> towns) {
        var stationEntities = velibStationRepository.findByNomCommuneIn(towns);

        var stationDtos = velibStationMapper.mapToDtoList(stationEntities);

        return new VelibStationResponseDto(stationDtos);
    }

    public VelibStationResponseDto getAllStations() {
        var allStationEntities = velibStationRepository.findAll();
        var stationDtos = velibStationMapper.mapToDtoList(allStationEntities);
        return new VelibStationResponseDto(stationDtos);
    }

    public VelibStationResponseDto getStationsByTown(String town) {
        var stationEntities = velibStationRepository.findByNomCommune(town);
        var stationDtos = velibStationMapper.mapToDtoList(stationEntities);
        return new VelibStationResponseDto(stationDtos);
    }

    public VelibStationResponseDto getAvailableStationsByTowns(List<String> towns) {
        var stationEntities = velibStationRepository.findByNomCommuneIn(towns);

        var availableStations = stationEntities.stream()
                .filter(station -> station.getBikesAvailable() > 0)
                .filter(station -> "OUI".equals(station.getIsRenting()))
                .toList();

        var stationDtos = velibStationMapper.mapToDtoList(availableStations);
        return new VelibStationResponseDto(stationDtos);
    }

    public List<String> getAvailableCommunes() {
        return velibStationRepository.findAllCommunes();
    }

    public StationStats getStationStats() {
        var allStations = velibStationRepository.findAll();

        long totalStations = allStations.size();
        long totalCommunes = allStations.stream()
                .map(VelibStationEntity::getNomCommune)
                .distinct()
                .count();
        long availableStations = allStations.stream()
                .filter(station -> station.getBikesAvailable() > 0)
                .filter(station -> "OUI".equals(station.getIsRenting()))
                .count();
        long totalBikes = allStations.stream()
                .mapToLong(VelibStationEntity::getBikesAvailable)
                .sum();
        long totalDocks = allStations.stream()
                .mapToLong(VelibStationEntity::getDocksAvailable)
                .sum();

        return new StationStats(
                totalStations,
                totalCommunes,
                availableStations,
                totalBikes,
                totalDocks
        );
    }
}
