package fr.esgi.rent.client;

import fr.esgi.rent.dto.velib.VelibStationRequestDto;
import fr.esgi.rent.dto.velib.VelibStationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static fr.esgi.rent.samples.VelibStationDtoSample.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VelibStationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VelibStationClient velibStationClient;

    private final String velibApiUrl = "http://localhost:8081";

    @BeforeEach
    void setUp() {
        velibStationClient = new VelibStationClient(restTemplate, velibApiUrl);
    }

    @Test
    void shouldGetStationsByTowns() {
        List<String> towns = Arrays.asList("Paris", "Lyon");
        VelibStationResponseDto expectedResponse = velibStationResponse();

        when(restTemplate.postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenReturn(expectedResponse);

        VelibStationResponseDto result = velibStationClient.getStationsByTowns(towns);

        assertNotNull(result);
        assertEquals(expectedResponse, result);
        assertEquals(2, result.stations().size());

        verify(restTemplate).postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldSendCorrectRequestBody() {
        List<String> towns = Arrays.asList("Paris", "Lyon", "Marseille");
        VelibStationResponseDto expectedResponse = velibStationResponse();

        ArgumentCaptor<HttpEntity> httpEntityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.postForObject(
                anyString(),
                httpEntityCaptor.capture(),
                eq(VelibStationResponseDto.class)
        )).thenReturn(expectedResponse);

        velibStationClient.getStationsByTowns(towns);

        HttpEntity<VelibStationRequestDto> capturedEntity = httpEntityCaptor.getValue();

        HttpHeaders headers = capturedEntity.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());

        VelibStationRequestDto requestBody = capturedEntity.getBody();
        assertNotNull(requestBody);
        assertEquals(towns, requestBody.towns());
    }

    @Test
    void shouldHandleSingleTown() {
        List<String> towns = Arrays.asList("Paris");
        VelibStationResponseDto expectedResponse = parisOnlyResponse();

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenReturn(expectedResponse);

        VelibStationResponseDto result = velibStationClient.getStationsByTowns(towns);

        assertNotNull(result);
        assertEquals(1, result.stations().size());
        assertEquals("Paris", result.stations().get(0).nomCommune());

        verify(restTemplate).postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldHandleEmptyTownsList() {
        List<String> emptyTowns = Arrays.asList();
        VelibStationResponseDto expectedResponse = emptyVelibResponse();

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenReturn(expectedResponse);

        VelibStationResponseDto result = velibStationClient.getStationsByTowns(emptyTowns);

        assertNotNull(result);
        assertTrue(result.stations().isEmpty());

        verify(restTemplate).postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldPropagateRestClientException() {
        List<String> towns = Arrays.asList("Paris");

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenThrow(new RestClientException("Connection timeout"));

        assertThrows(RestClientException.class, () -> {
            velibStationClient.getStationsByTowns(towns);
        });

        verify(restTemplate).postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldHandleNullResponse() {
        List<String> towns = Arrays.asList("Paris");

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenReturn(null);

        VelibStationResponseDto result = velibStationClient.getStationsByTowns(towns);

        assertNull(result);

        verify(restTemplate).postForObject(
                eq(velibApiUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldUseCorrectApiUrl() {
        List<String> towns = Arrays.asList("Lyon");
        String customUrl = "http://custom-api.example.com";
        VelibStationClient customClient = new VelibStationClient(restTemplate, customUrl);

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenReturn(emptyVelibResponse());

        customClient.getStationsByTowns(towns);

        verify(restTemplate).postForObject(
                eq(customUrl + "/stations/velibs"),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        );
    }

    @Test
    void shouldVerifyRequestStructure() {
        List<String> towns = Arrays.asList("Paris", "Lyon");
        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.postForObject(
                urlCaptor.capture(),
                entityCaptor.capture(),
                eq(VelibStationResponseDto.class)
        )).thenReturn(velibStationResponse());

        velibStationClient.getStationsByTowns(towns);

        assertEquals(velibApiUrl + "/stations/velibs", urlCaptor.getValue());

        HttpEntity<VelibStationRequestDto> entity = entityCaptor.getValue();
        assertNotNull(entity.getBody());
        assertNotNull(entity.getHeaders());
        assertEquals(MediaType.APPLICATION_JSON, entity.getHeaders().getContentType());
        assertEquals(towns, entity.getBody().towns());
    }

    @Test
    void shouldHandleNetworkException() {
        List<String> towns = Arrays.asList("Paris");

        when(restTemplate.postForObject(
                anyString(),
                any(HttpEntity.class),
                eq(VelibStationResponseDto.class)
        )).thenThrow(new RuntimeException("Network error"));

        assertThrows(RuntimeException.class, () -> {
            velibStationClient.getStationsByTowns(towns);
        });
    }
}
