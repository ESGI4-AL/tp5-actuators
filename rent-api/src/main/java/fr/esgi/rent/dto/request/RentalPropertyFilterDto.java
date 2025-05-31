package fr.esgi.rent.dto.request;

import lombok.Builder;

@Builder
public record RentalPropertyFilterDto(
        Boolean nearVelibStations) {
}
