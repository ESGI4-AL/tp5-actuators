package fr.esgi.rent.dto.velib;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VelibStationResponseDto(List<StationDto> stations) {

    public record StationDto(
            @NotNull String stationCode,
            @NotNull String name,
            @NotNull String isInstalled,
            int capacity,
            int docksAvailable,
            int bikesAvailable,
            int mechanical,
            int eBike,
            @NotNull String isRenting,
            @NotNull String isReturning,
            @NotNull String dueDate,
            @NotNull CoordinatesDto coordonneesGeo,
            @NotNull String nomCommune,
            @NotNull String codeInseeCommune,
            @NotNull String stationOpeningHours
    ) {}

    public record CoordinatesDto(
            double lon,
            double lat
    ) {}
}
