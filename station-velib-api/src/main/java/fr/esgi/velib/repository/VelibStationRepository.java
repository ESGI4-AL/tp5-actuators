package fr.esgi.velib.repository;

import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.service.DataService;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VelibStationRepository {

    private final DataService dataService;

    public VelibStationRepository(DataService dataService) {
        this.dataService = dataService;
    }

    public List<VelibStationEntity> findAll() {
        return dataService.getAllStations();
    }

    public List<VelibStationEntity> findByNomCommuneIn(List<String> communes) {
        return dataService.getAllStations().stream()
                .filter(station -> communes.contains(station.getNomCommune()))
                .toList();
    }

    public List<VelibStationEntity> findByNomCommune(String commune) {
        return dataService.getAllStations().stream()
                .filter(station -> commune.equals(station.getNomCommune()))
                .toList();
    }

    public List<String> findAllCommunes() {
        return dataService.getAvailableCommunes();
    }
}
