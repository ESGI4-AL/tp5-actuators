package samples;

import fr.esgi.velib.dto.request.VelibStationRequestDto;
import fr.esgi.velib.dto.response.VelibStationResponseDto;

import java.util.Arrays;

public class VelibStationDtoSample {

    public static VelibStationRequestDto parisLyonRequest() {
        return new VelibStationRequestDto(Arrays.asList("Paris", "Lyon"));
    }

    public static VelibStationRequestDto parisOnlyRequest() {
        return new VelibStationRequestDto(Arrays.asList("Paris"));
    }

    public static VelibStationRequestDto emptyRequest() {
        return new VelibStationRequestDto(Arrays.asList());
    }

    public static VelibStationRequestDto unknownTownRequest() {
        return new VelibStationRequestDto(Arrays.asList("UnknownCity"));
    }

    public static VelibStationResponseDto.StationDto parisStationDto() {
        return new VelibStationResponseDto.StationDto(
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
                new VelibStationResponseDto.CoordinatesDto(2.3522, 48.8566),
                "Paris",
                "75001",
                "24/7"
        );
    }

    public static VelibStationResponseDto.StationDto lyonStationDto() {
        return new VelibStationResponseDto.StationDto(
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
                new VelibStationResponseDto.CoordinatesDto(4.8357, 45.7640),
                "Lyon",
                "69001",
                "24/7"
        );
    }

    public static VelibStationResponseDto.StationDto marseilleStationDto() {
        return new VelibStationResponseDto.StationDto(
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
                new VelibStationResponseDto.CoordinatesDto(5.3698, 43.2965),
                "Marseille",
                "13001",
                "06:00-22:00"
        );
    }

    public static VelibStationResponseDto.StationDto emptyStationDto() {
        return new VelibStationResponseDto.StationDto(
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
                new VelibStationResponseDto.CoordinatesDto(2.3420, 48.8670),
                "Paris",
                "75002",
                "24/7"
        );
    }

    public static VelibStationResponseDto allStationsResponse() {
        return new VelibStationResponseDto(Arrays.asList(
                parisStationDto(),
                lyonStationDto(),
                marseilleStationDto(),
                emptyStationDto()
        ));
    }

    public static VelibStationResponseDto parisStationsResponse() {
        return new VelibStationResponseDto(Arrays.asList(
                parisStationDto(),
                emptyStationDto()
        ));
    }

    public static VelibStationResponseDto lyonStationsResponse() {
        return new VelibStationResponseDto(Arrays.asList(
                lyonStationDto()
        ));
    }

    public static VelibStationResponseDto availableStationsResponse() {
        return new VelibStationResponseDto(Arrays.asList(
                parisStationDto(),
                lyonStationDto(),
                marseilleStationDto()
        ));
    }

    public static VelibStationResponseDto emptyResponse() {
        return new VelibStationResponseDto(Arrays.asList());
    }
}
