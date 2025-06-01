package samples;

import fr.esgi.velib.domain.VelibStationEntity;

import java.util.Arrays;
import java.util.List;

public class VelibStationEntitySample {

    public static VelibStationEntity parisStation() {
        return new VelibStationEntity(
                "75001-01",
                "République",
                "OUI",
                30,
                10,
                20,
                15,
                5,
                "OUI",
                "OUI",
                "2024-12-31T23:59:59+00:00",
                2.3522,
                48.8566,
                "Paris",
                "75001",
                "24/7"
        );
    }

    public static VelibStationEntity lyonStation() {
        return new VelibStationEntity(
                "69001-01",
                "Bellecour",
                "OUI",
                25,
                8,
                17,
                12,
                5,
                "OUI",
                "OUI",
                "2024-12-31T23:59:59+00:00",
                4.8357,
                45.7640,
                "Lyon",
                "69001",
                "24/7"
        );
    }

    public static VelibStationEntity marseilleStation() {
        return new VelibStationEntity(
                "13001-01",
                "Vieux Port",
                "OUI",
                20,
                5,
                15,
                10,
                5,
                "OUI",
                "OUI",
                "2024-12-31T23:59:59+00:00",
                5.3698,
                43.2965,
                "Marseille",
                "13001",
                "06:00-22:00"
        );
    }

    public static VelibStationEntity emptyStation() {
        return new VelibStationEntity(
                "75002-01",
                "Station Vide",
                "OUI",
                20,
                20,
                0,
                0,
                0,
                "NON",
                "OUI",
                "2024-12-31T23:59:59+00:00",
                2.3420,
                48.8670,
                "Paris",
                "75002",
                "24/7"
        );
    }

    public static VelibStationEntity maintenanceStation() {
        return new VelibStationEntity(
                "69002-01",
                "Station Maintenance",
                "NON",
                15,
                0,
                0,
                0,
                0,
                "NON",
                "NON",
                "2024-12-31T23:59:59+00:00",
                4.8400,
                45.7700,
                "Lyon",
                "69002",
                null
        );
    }

    public static List<VelibStationEntity> allStations() {
        return Arrays.asList(
                parisStation(),
                lyonStation(),
                marseilleStation(),
                emptyStation(),
                maintenanceStation()
        );
    }

    public static List<VelibStationEntity> parisStations() {
        return Arrays.asList(
                parisStation(),
                emptyStation()
        );
    }

    public static List<VelibStationEntity> availableStations() {
        return Arrays.asList(
                parisStation(),
                lyonStation(),
                marseilleStation()
        );
    }

    public static List<VelibStationEntity> emptyList() {
        return Arrays.asList();
    }
}
