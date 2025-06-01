package fr.esgi.velib.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.velib.domain.VelibStationEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DataService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<VelibStationEntity> stations = new ArrayList<>();

    @PostConstruct
    public void loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("data/velib-stations.json");
            InputStream inputStream = resource.getInputStream();

            JsonNode rootNode = objectMapper.readTree(inputStream);
            JsonNode stationsNode = rootNode.get("stations");

            if (stationsNode != null && stationsNode.isArray()) {
                for (JsonNode stationNode : stationsNode) {
                    VelibStationEntity station = mapJsonToEntity(stationNode);
                    stations.add(station);
                }
            }

            stations.stream()
                    .map(VelibStationEntity::getNomCommune)
                    .distinct()
                    .count();

        } catch (IOException e) {
            loadDefaultData();
        }
    }

    public List<VelibStationEntity> getAllStations() {
        return new ArrayList<>(stations);
    }

    public List<String> getAvailableCommunes() {
        return stations.stream()
                .map(VelibStationEntity::getNomCommune)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();
    }

    private VelibStationEntity mapJsonToEntity(JsonNode stationNode) {
        JsonNode coordsNode = stationNode.get("coordonnees_geo");

        return new VelibStationEntity(
                getStringValue(stationNode, "stationcode"),
                getStringValue(stationNode, "name"),
                getStringValue(stationNode, "is_installed"),
                getIntValue(stationNode, "capacity"),
                getIntValue(stationNode, "numdocksavailable"),
                getIntValue(stationNode, "numbikesavailable"),
                getIntValue(stationNode, "mechanical"),
                getIntValue(stationNode, "ebike"),
                getStringValue(stationNode, "is_renting"),
                getStringValue(stationNode, "is_returning"),
                getStringValue(stationNode, "duedate"),
                coordsNode != null ? coordsNode.get("lon").asDouble() : 0.0,
                coordsNode != null ? coordsNode.get("lat").asDouble() : 0.0,
                getStringValue(stationNode, "nom_commune"),
                getStringValue(stationNode, "code_insee_commune"),
                stationNode.has("station_opening_hours") && !stationNode.get("station_opening_hours").isNull()
                        ? stationNode.get("station_opening_hours").asText()
                        : null
        );
    }

    private String getStringValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asText() : "";
    }

    private int getIntValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        return fieldNode != null ? fieldNode.asInt() : 0;
    }

    private void loadDefaultData() {
        stations = new ArrayList<>();
        stations.add(new VelibStationEntity(
                "DEFAULT001",
                "Station par défaut",
                "OUI",
                30,
                10,
                20,
                15,
                5,
                "OUI",
                "OUI",
                "2025-04-23T12:00:00+00:00",
                2.3522,
                48.8566,
                "Paris",
                "75001",
                null
        ));
    }
}
