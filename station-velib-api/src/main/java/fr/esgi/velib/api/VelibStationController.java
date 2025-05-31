package fr.esgi.velib.api;


import fr.esgi.velib.dto.request.VelibStationRequestDto;
import fr.esgi.velib.dto.response.VelibStationResponseDto;
import fr.esgi.velib.service.VelibStationService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class VelibStationController {

    private final VelibStationService velibStationService;

    public VelibStationController(VelibStationService velibStationService) {
        this.velibStationService = velibStationService;
    }

    @GetMapping("/health")
    public String test() {
        return "Station Velib API is running with JSON data! " +
                velibStationService.getStationStats().totalStations() + " stations loaded.";
    }

    @PostMapping("/velibs")
    public VelibStationResponseDto getVelibStations(@RequestBody VelibStationRequestDto request) {
        return velibStationService.getStationsByTowns(request.towns());
    }

    @GetMapping("/velibs")
    public VelibStationResponseDto getAllStations() {
        return velibStationService.getAllStations();
    }

    @GetMapping("/velibs/{town}")
    public VelibStationResponseDto getStationsByTown(@PathVariable String town) {
        return velibStationService.getStationsByTown(town);
    }

    @PostMapping("/velibs/available")
    public VelibStationResponseDto getAvailableStations(@RequestBody VelibStationRequestDto request) {
        return velibStationService.getAvailableStationsByTowns(request.towns());
    }

    @GetMapping("/communes")
    public List<String> getAvailableCommunes() {
        return velibStationService.getAvailableCommunes();
    }

    @GetMapping("/stats")
    public StationStats getStats() {
        return velibStationService.getStationStats();
    }

    public record StationStats(
            long totalStations,
            long totalCommunes,
            long availableStations,
            long totalBikes,
            long totalDocks
    ) {}
}
