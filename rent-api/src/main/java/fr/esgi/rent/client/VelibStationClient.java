package fr.esgi.rent.client;

import fr.esgi.rent.dto.velib.VelibStationRequestDto;
import fr.esgi.rent.dto.velib.VelibStationResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class VelibStationClient {

    private final RestTemplate restTemplate;
    private final String velibApiUrl;

    public VelibStationClient(RestTemplate restTemplate,
                              @Value("${velib.stations.api.url}") String velibApiUrl) {
        this.restTemplate = restTemplate;
        this.velibApiUrl = velibApiUrl;
    }

    public VelibStationResponseDto getStationsByTowns(List<String> towns) {
        var request = new VelibStationRequestDto(towns);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VelibStationRequestDto> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(
                velibApiUrl + "/stations/velibs",
                entity,
                VelibStationResponseDto.class
        );
    }
}
