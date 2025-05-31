package fr.esgi.rent.samples;

import fr.esgi.rent.dto.velib.VelibStationRequestDto;
import fr.esgi.rent.dto.velib.VelibStationResponseDto;

import java.util.Arrays;

public class VelibStationDtoSample {

    public static VelibStationRequestDto velibStationRequest() {
        return new VelibStationRequestDto(Arrays.asList("Paris", "Lyon", "Marseille"));
    }

    public static VelibStationRequestDto singleTownRequest() {
        return new VelibStationRequestDto(Arrays.asList("Paris"));
    }

    public static VelibStationResponseDto velibStationResponse() {
        VelibStationResponseDto.CoordinatesDto parisCoordinates =
                new VelibStationResponseDto.CoordinatesDto(2.3522, 48.8566);

        VelibStationResponseDto.StationDto parisStation =
                new VelibStationResponseDto.StationDto(
                        "001",
                        "Station République",
                        "OUI",
                        20,
                        5,
                        15,
                        10,
                        5,
                        "OUI",
                        "OUI",
                        "2024-12-31",
                        parisCoordinates,
                        "Paris",
                        "75003",
                        "24/7"
                );

        VelibStationResponseDto.CoordinatesDto lyonCoordinates =
                new VelibStationResponseDto.CoordinatesDto(4.8357, 45.7640);

        VelibStationResponseDto.StationDto lyonStation =
                new VelibStationResponseDto.StationDto(
                        "002",
                        "Station Bellecour",
                        "OUI",
                        15,
                        3,
                        12,
                        8,
                        4,
                        "OUI",
                        "OUI",
                        "2024-12-31",
                        lyonCoordinates,
                        "Lyon",
                        "69002",
                        "24/7"
                );

        return new VelibStationResponseDto(Arrays.asList(parisStation, lyonStation));
    }

    public static VelibStationResponseDto emptyVelibResponse() {
        return new VelibStationResponseDto(Arrays.asList());
    }

    public static VelibStationResponseDto nullStationsResponse() {
        return new VelibStationResponseDto(null);
    }

    public static VelibStationResponseDto parisOnlyResponse() {
        VelibStationResponseDto.CoordinatesDto coordinates =
                new VelibStationResponseDto.CoordinatesDto(2.3522, 48.8566);

        VelibStationResponseDto.StationDto station =
                new VelibStationResponseDto.StationDto(
                        "001",
                        "Station République",
                        "OUI",
                        20,
                        5,
                        15,
                        10,
                        5,
                        "OUI",
                        "OUI",
                        "2024-12-31",
                        coordinates,
                        "Paris",
                        "75003",
                        "24/7"
                );

        return new VelibStationResponseDto(Arrays.asList(station));
    }
}
