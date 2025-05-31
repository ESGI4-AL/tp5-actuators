package fr.esgi.rent.samples;

import fr.esgi.rent.dto.request.RentalPropertyFilterDto;

public class RentalPropertyFilterDtoSample {

    public static RentalPropertyFilterDto noFilter() {
        return new RentalPropertyFilterDto(null);
    }

    public static RentalPropertyFilterDto withVelibStations() {
        return new RentalPropertyFilterDto(true);
    }

    public static RentalPropertyFilterDto withoutVelibStations() {
        return new RentalPropertyFilterDto(false);
    }
}
