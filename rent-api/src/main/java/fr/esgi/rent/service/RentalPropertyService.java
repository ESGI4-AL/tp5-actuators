package fr.esgi.rent.service;

import fr.esgi.rent.client.VelibStationClient;
import fr.esgi.rent.domain.RentalPropertyEntity;
import fr.esgi.rent.dto.request.RentalPropertyFilterDto;
import fr.esgi.rent.dto.velib.VelibStationResponseDto;
import fr.esgi.rent.repository.RentalPropertyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RentalPropertyService {

    private final RentalPropertyRepository rentalPropertyRepository;
    private final VelibStationClient velibStationClient;

    public RentalPropertyService(RentalPropertyRepository rentalPropertyRepository,
                                 VelibStationClient velibStationClient) {
        this.rentalPropertyRepository = rentalPropertyRepository;
        this.velibStationClient = velibStationClient;
    }

    public List<RentalPropertyEntity> findRentalProperties(RentalPropertyFilterDto filter) {
        List<RentalPropertyEntity> allProperties = rentalPropertyRepository.findAll();

        if (filter != null && Boolean.TRUE.equals(filter.nearVelibStations())) {
            return filterPropertiesNearVelibStations(allProperties);
        }

        return allProperties;
    }

    private List<RentalPropertyEntity> filterPropertiesNearVelibStations(List<RentalPropertyEntity> properties) {
        List<String> propertyTowns = properties.stream()
                .map(RentalPropertyEntity::getTown)
                .distinct()
                .toList();


        try {
            var velibResponse = velibStationClient.getStationsByTowns(propertyTowns);

            if (velibResponse == null || velibResponse.stations() == null || velibResponse.stations().isEmpty()) {
                return List.of();
            }

            Set<String> townsWithVelibStations = velibResponse.stations().stream()
                    .map(VelibStationResponseDto.StationDto::nomCommune)
                    .collect(Collectors.toSet());

            return properties.stream()
                    .filter(property -> townsWithVelibStations.contains(property.getTown()))
                    .toList();

        } catch (Exception e) {
            return properties;
        }
    }
}
