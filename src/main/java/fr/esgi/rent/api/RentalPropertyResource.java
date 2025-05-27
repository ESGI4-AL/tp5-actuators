package fr.esgi.rent.api;

import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.mapper.RentalPropertyMapper;
import fr.esgi.rent.repository.RentalPropertyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RentalPropertyResource {

    private final RentalPropertyRepository rentalPropertyRepository;
    private final RentalPropertyMapper rentalPropertyMapper;

    public RentalPropertyResource(RentalPropertyRepository rentalPropertyRepository,
                                  RentalPropertyMapper rentalPropertyMapper) {
        this.rentalPropertyRepository = rentalPropertyRepository;
        this.rentalPropertyMapper = rentalPropertyMapper;
    }

    @GetMapping("/rental-properties")
    public List<RentalPropertyResponseDto> getRentalProperties() {
        var rentalProperties = rentalPropertyRepository.findAll();
        return rentalPropertyMapper.mapToDtoList(rentalProperties);
    }

    @GetMapping("/rental-properties/{id}")
    public ResponseEntity<RentalPropertyResponseDto> getRentalProperty(@PathVariable UUID id) {
        var rentalProperty = rentalPropertyRepository.findById(id);

        if (rentalProperty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var dto = rentalPropertyMapper.mapToDto(rentalProperty.get());
        return ResponseEntity.ok(dto);
    }
}
