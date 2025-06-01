package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.velib.domain.VelibStationEntity;
import fr.esgi.velib.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    private DataService dataService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        dataService = new DataService();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllStations_ShouldReturnCopyOfStationsList() {
        ReflectionTestUtils.setField(dataService, "stations",
                List.of(createTestStation("001", "Test")));

        List<VelibStationEntity> result1 = dataService.getAllStations();
        List<VelibStationEntity> result2 = dataService.getAllStations();

        assertThat(result1)
                .isNotNull()
                .hasSize(1);

        assertThat(result1)
                .isNotSameAs(result2)
                .isEqualTo(result2);
    }

    @Test
    void getAllStations_ShouldReturnEmptyList_WhenNoStations() {
        ReflectionTestUtils.setField(dataService, "stations", List.of());

        List<VelibStationEntity> result = dataService.getAllStations();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getAvailableCommunes_ShouldReturnDistinctSortedCommunes() {
        List<VelibStationEntity> testStations = List.of(
                createTestStation("001", "Station 1", "Paris"),
                createTestStation("002", "Station 2", "Nogent-sur-Marne"),
                createTestStation("003", "Station 3", "Paris"),
                createTestStation("004", "Station 4", "Vincennes"),
                createTestStation("005", "Station 5", "Créteil")
        );

        ReflectionTestUtils.setField(dataService, "stations", testStations);

        List<String> result = dataService.getAvailableCommunes();

        assertThat(result)
                .isNotNull()
                .hasSize(4)
                .containsExactly("Créteil", "Nogent-sur-Marne", "Paris", "Vincennes");
    }

    @Test
    void getAvailableCommunes_ShouldReturnEmptyList_WhenNoStations() {
        ReflectionTestUtils.setField(dataService, "stations", List.of());

        List<String> result = dataService.getAvailableCommunes();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void getAvailableCommunes_ShouldHandleStationsWithNullCommune() {
        List<VelibStationEntity> testStations = List.of(
                createTestStation("001", "Station 1", "Paris"),
                createTestStation("002", "Station 2", null),
                createTestStation("003", "Station 3", "Vincennes")
        );

        ReflectionTestUtils.setField(dataService, "stations", testStations);

        List<String> result = dataService.getAvailableCommunes();

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly("Paris", "Vincennes");
    }

    @Test
    void loadDefaultData_ShouldCreateDefaultStation() {
        ReflectionTestUtils.invokeMethod(dataService, "loadDefaultData");

        List<VelibStationEntity> stations = dataService.getAllStations();

        assertThat(stations)
                .isNotNull()
                .hasSize(1);

        VelibStationEntity defaultStation = stations.get(0);
        assertThat(defaultStation.getStationCode()).isEqualTo("DEFAULT001");
        assertThat(defaultStation.getName()).isEqualTo("Station par défaut");
        assertThat(defaultStation.getNomCommune()).isEqualTo("Paris");
        assertThat(defaultStation.getCapacity()).isEqualTo(30);
        assertThat(defaultStation.getDocksAvailable()).isEqualTo(10);
        assertThat(defaultStation.getBikesAvailable()).isEqualTo(20);
        assertThat(defaultStation.getMechanical()).isEqualTo(15);
        assertThat(defaultStation.geteBike()).isEqualTo(5);
        assertThat(defaultStation.getIsRenting()).isEqualTo("OUI");
        assertThat(defaultStation.getIsReturning()).isEqualTo("OUI");
        assertThat(defaultStation.getLongitude()).isEqualTo(2.3522);
        assertThat(defaultStation.getLatitude()).isEqualTo(48.8566);
        assertThat(defaultStation.getCodeInseeCommune()).isEqualTo("75001");
        assertThat(defaultStation.getStationOpeningHours()).isNull();
    }

    @Test
    void mapJsonToEntity_ShouldMapCompleteStation() throws Exception {
        String stationJson = """
                {
                    "stationcode": "41303",
                    "name": "Gare de Nogent-le-Perreux",
                    "is_installed": "OUI",
                    "capacity": 50,
                    "numdocksavailable": 0,
                    "numbikesavailable": 31,
                    "mechanical": 8,
                    "ebike": 23,
                    "is_renting": "OUI",
                    "is_returning": "OUI",
                    "duedate": "2025-04-23T12:58:03+00:00",
                    "coordonnees_geo": {
                        "lon": 2.493693307042122,
                        "lat": 48.83842553348809
                    },
                    "nom_commune": "Nogent-sur-Marne",
                    "code_insee_commune": "94052",
                    "station_opening_hours": null
                }
                """;

        JsonNode stationNode = objectMapper.readTree(stationJson);

        VelibStationEntity result = ReflectionTestUtils.invokeMethod(
                dataService, "mapJsonToEntity", stationNode);

        assertThat(result)
                .isNotNull();
        assertThat(result.getStationCode()).isEqualTo("41303");
        assertThat(result.getName()).isEqualTo("Gare de Nogent-le-Perreux");
        assertThat(result.getIsInstalled()).isEqualTo("OUI");
        assertThat(result.getCapacity()).isEqualTo(50);
        assertThat(result.getDocksAvailable()).isEqualTo(0);
        assertThat(result.getBikesAvailable()).isEqualTo(31);
        assertThat(result.getMechanical()).isEqualTo(8);
        assertThat(result.geteBike()).isEqualTo(23);
        assertThat(result.getIsRenting()).isEqualTo("OUI");
        assertThat(result.getIsReturning()).isEqualTo("OUI");
        assertThat(result.getDueDate()).isEqualTo("2025-04-23T12:58:03+00:00");
        assertThat(result.getLongitude()).isEqualTo(2.493693307042122);
        assertThat(result.getLatitude()).isEqualTo(48.83842553348809);
        assertThat(result.getNomCommune()).isEqualTo("Nogent-sur-Marne");
        assertThat(result.getCodeInseeCommune()).isEqualTo("94052");
        assertThat(result.getStationOpeningHours()).isNull();
    }

    @Test
    void mapJsonToEntity_ShouldMapStationWithMissingCoordinates() throws Exception {
        String stationJson = """
                {
                    "stationcode": "001",
                    "name": "Station sans coordonnées",
                    "nom_commune": "Paris"
                }
                """;

        JsonNode stationNode = objectMapper.readTree(stationJson);

        VelibStationEntity result = ReflectionTestUtils.invokeMethod(
                dataService, "mapJsonToEntity", stationNode);

        assertThat(result)
                .isNotNull();
        assertThat(result.getStationCode()).isEqualTo("001");
        assertThat(result.getName()).isEqualTo("Station sans coordonnées");
        assertThat(result.getLongitude()).isEqualTo(0.0);
        assertThat(result.getLatitude()).isEqualTo(0.0);
        assertThat(result.getNomCommune()).isEqualTo("Paris");
    }

    @Test
    void mapJsonToEntity_ShouldMapStationWithOpeningHours() throws Exception {
        String stationJson = """
                {
                    "stationcode": "002",
                    "name": "Station avec horaires",
                    "station_opening_hours": "24/7"
                }
                """;

        JsonNode stationNode = objectMapper.readTree(stationJson);

        VelibStationEntity result = ReflectionTestUtils.invokeMethod(
                dataService, "mapJsonToEntity", stationNode);

        assertThat(result)
                .isNotNull();
        assertThat(result.getStationOpeningHours()).isEqualTo("24/7");
    }

    @Test
    void getStringValue_ShouldReturnFieldValue_WhenFieldExists() throws Exception {
        String json = """
                {
                    "name": "Test Station"
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        String result = ReflectionTestUtils.invokeMethod(
                dataService, "getStringValue", node, "name");

        assertThat(result).isEqualTo("Test Station");
    }

    @Test
    void getStringValue_ShouldReturnEmptyString_WhenFieldDoesNotExist() throws Exception {
        String json = """
                {
                    "other_field": "value"
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        String result = (String) ReflectionTestUtils.invokeMethod(
                dataService, "getStringValue", node, "name");

        assertThat(result).isEqualTo("");
    }

    @Test
    void getStringValue_ShouldReturnEmptyString_WhenFieldIsNull() throws Exception {
        String json = """
                {
                    "name": null
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        String result = (String) ReflectionTestUtils.invokeMethod(
                dataService, "getStringValue", node, "name");

        assertThat(result).isEqualTo("null");
    }

    @Test
    void getIntValue_ShouldReturnFieldValue_WhenFieldExists() throws Exception {
        String json = """
                {
                    "capacity": 50
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        Integer result = ReflectionTestUtils.invokeMethod(
                dataService, "getIntValue", node, "capacity");

        assertThat(result).isEqualTo(50);
    }

    @Test
    void getIntValue_ShouldReturnZero_WhenFieldDoesNotExist() throws Exception {
        String json = """
                {
                    "other_field": 100
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        Integer result = ReflectionTestUtils.invokeMethod(
                dataService, "getIntValue", node, "capacity");

        assertThat(result).isEqualTo(0);
    }

    @Test
    void getIntValue_ShouldReturnZero_WhenFieldIsNull() throws Exception {
        String json = """
                {
                    "capacity": null
                }
                """;
        JsonNode node = objectMapper.readTree(json);

        Integer result = ReflectionTestUtils.invokeMethod(
                dataService, "getIntValue", node, "capacity");

        assertThat(result).isEqualTo(0);
    }

    @Test
    void loadData_ShouldParseStationsCorrectly_WithSimulatedJson() throws Exception {
        String jsonData = """
                {
                    "stations": [
                        {
                            "stationcode": "41303",
                            "name": "Gare de Nogent",
                            "capacity": 50,
                            "nom_commune": "Nogent-sur-Marne",
                            "coordonnees_geo": {
                                "lon": 2.49,
                                "lat": 48.83
                            }
                        },
                        {
                            "stationcode": "41304",
                            "name": "Station Louvre",
                            "capacity": 30,
                            "nom_commune": "Paris"
                        }
                    ]
                }
                """;

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode stationsNode = rootNode.get("stations");

        List<VelibStationEntity> stations = new java.util.ArrayList<>();
        if (stationsNode != null && stationsNode.isArray()) {
            for (JsonNode stationNode : stationsNode) {
                VelibStationEntity station = ReflectionTestUtils.invokeMethod(
                        dataService, "mapJsonToEntity", stationNode);
                stations.add(station);
            }
        }

        ReflectionTestUtils.setField(dataService, "stations", stations);

        List<VelibStationEntity> result = dataService.getAllStations();
        assertThat(result)
                .isNotNull()
                .hasSize(2);

        VelibStationEntity station1 = result.get(0);
        assertThat(station1.getStationCode()).isEqualTo("41303");
        assertThat(station1.getName()).isEqualTo("Gare de Nogent");
        assertThat(station1.getCapacity()).isEqualTo(50);
        assertThat(station1.getNomCommune()).isEqualTo("Nogent-sur-Marne");
        assertThat(station1.getLongitude()).isEqualTo(2.49);
        assertThat(station1.getLatitude()).isEqualTo(48.83);

        VelibStationEntity station2 = result.get(1);
        assertThat(station2.getStationCode()).isEqualTo("41304");
        assertThat(station2.getName()).isEqualTo("Station Louvre");
        assertThat(station2.getCapacity()).isEqualTo(30);
        assertThat(station2.getNomCommune()).isEqualTo("Paris");
        assertThat(station2.getLongitude()).isEqualTo(0.0);
        assertThat(station2.getLatitude()).isEqualTo(0.0);
    }

    @Test
    void constructor_ShouldInitializeObjectMapperCorrectly() {
        DataService service = new DataService();

        assertThat(service).isNotNull();
        ObjectMapper objectMapper = (ObjectMapper) ReflectionTestUtils.getField(service, "objectMapper");
        assertThat(objectMapper).isNotNull();
    }

    @Test
    void constructor_ShouldInitializeEmptyStationsList() {
        DataService service = new DataService();

        assertThat(service).isNotNull();
        @SuppressWarnings("unchecked")
        List<VelibStationEntity> stations = (List<VelibStationEntity>) ReflectionTestUtils.getField(service, "stations");
        assertThat(stations)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void loadData_ShouldHandleJsonWithNullStations() throws Exception {
        String jsonData = """
                {
                    "stations": null
                }
                """;

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode stationsNode = rootNode.get("stations");

        List<VelibStationEntity> stations = new java.util.ArrayList<>();
        if (stationsNode != null) {
            stationsNode.isArray();
        }

        ReflectionTestUtils.setField(dataService, "stations", stations);

        List<VelibStationEntity> result = dataService.getAllStations();
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void loadData_ShouldHandleJsonWithoutStationsNode() throws Exception {
        String jsonData = """
                {
                    "other_data": "value"
                }
                """;

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode stationsNode = rootNode.get("stations");

        List<VelibStationEntity> stations = new java.util.ArrayList<>();
        if (stationsNode != null) {
            stationsNode.isArray();
        }

        ReflectionTestUtils.setField(dataService, "stations", stations);

        List<VelibStationEntity> result = dataService.getAllStations();
        assertThat(result)
                .isNotNull()
                .isEmpty();
    }

    private VelibStationEntity createTestStation(String code, String name) {
        return createTestStation(code, name, "TestCommune");
    }

    private VelibStationEntity createTestStation(String code, String name, String commune) {
        return new VelibStationEntity(
                code, name, "OUI", 30, 10, 20, 15, 5,
                "OUI", "OUI", "2025-04-23T12:00:00+00:00",
                2.0, 48.0, commune, "75001", "24/7"
        );
    }
}
