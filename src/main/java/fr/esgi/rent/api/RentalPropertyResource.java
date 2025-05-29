package fr.esgi.rent.api;

import fr.esgi.rent.dto.request.RentalPropertyRequestDto;
import fr.esgi.rent.dto.response.RentalPropertyResponseDto;
import fr.esgi.rent.mapper.RentalPropertyMapper;
import fr.esgi.rent.repository.RentalPropertyRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/rental-properties")
    public ResponseEntity<RentalPropertyResponseDto> createRentalProperty(
            @Valid @RequestBody RentalPropertyRequestDto requestDto) {

        try {
            var entity = rentalPropertyMapper.mapToEntity(requestDto);
            var savedEntity = rentalPropertyRepository.save(entity);
            var responseDto = rentalPropertyMapper.mapToDto(savedEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
