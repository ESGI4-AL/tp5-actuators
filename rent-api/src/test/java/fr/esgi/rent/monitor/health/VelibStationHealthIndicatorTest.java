package fr.esgi.rent.monitor.health;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VelibStationHealthIndicatorTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private VelibStationHealthIndicator healthIndicator;

    @Test
    void should_return_up_when_velib_api_is_healthy() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        Map<String, Object> mockResponse = Map.of("status", "UP");
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals("Service is available", health.getDetails().get("velib-stations-api"));
        assertEquals("http://localhost:8081/actuator/health", health.getDetails().get("url"));
        assertEquals("UP", health.getDetails().get("response-status"));
    }

    @Test
    void should_return_down_when_velib_api_returns_down_status() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        Map<String, Object> mockResponse = Map.of("status", "DOWN");
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service returned non-UP status", health.getDetails().get("velib-stations-api"));
        assertEquals("http://localhost:8081/actuator/health", health.getDetails().get("url"));
        assertEquals("DOWN", health.getDetails().get("response-status"));
        assertEquals(mockResponse, health.getDetails().get("full-response"));
    }

    @Test
    void should_return_down_when_velib_api_returns_unknown_status() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        Map<String, Object> mockResponse = Map.of("status", "UNKNOWN");
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service returned non-UP status", health.getDetails().get("velib-stations-api"));
        assertEquals("UNKNOWN", health.getDetails().get("response-status"));
        assertEquals(mockResponse, health.getDetails().get("full-response"));
    }

    @Test
    void should_return_down_when_response_has_no_status() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        Map<String, Object> mockResponse = Map.of("components", "some data");
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service is unavailable", health.getDetails().get("velib-stations-api"));
        assertEquals("http://localhost:8081/actuator/health", health.getDetails().get("url"));
        assertNotNull(health.getDetails().get("error"));
    }

    @Test
    void should_return_down_when_response_is_null() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(null);

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service is unavailable", health.getDetails().get("velib-stations-api"));
        assertEquals("http://localhost:8081/actuator/health", health.getDetails().get("url"));
        assertNotNull(health.getDetails().get("error"));
        assertEquals("NullPointerException", health.getDetails().get("error-type"));
    }

    @Test
    void should_return_down_when_rest_client_throws_exception() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        when(restTemplate.getForObject(any(String.class), eq(Map.class)))
                .thenThrow(new RestClientException("Connection refused"));

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service is unavailable", health.getDetails().get("velib-stations-api"));
        assertEquals("http://localhost:8081/actuator/health", health.getDetails().get("url"));
        assertEquals("Connection refused", health.getDetails().get("error"));
        assertEquals("RestClientException", health.getDetails().get("error-type"));
    }

    @Test
    void should_return_down_when_timeout_exception_occurs() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        when(restTemplate.getForObject(any(String.class), eq(Map.class)))
                .thenThrow(new RuntimeException("Read timeout"));

        Health health = healthIndicator.health();

        assertEquals(Status.DOWN, health.getStatus());
        assertEquals("Service is unavailable", health.getDetails().get("velib-stations-api"));
        assertEquals("Read timeout", health.getDetails().get("error"));
        assertEquals("RuntimeException", health.getDetails().get("error-type"));
    }


    @Test
    void should_handle_custom_api_url() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://custom-host:9999");
        Map<String, Object> mockResponse = Map.of("status", "UP");
        when(restTemplate.getForObject("http://custom-host:9999/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals("http://custom-host:9999/actuator/health", health.getDetails().get("url"));
        assertEquals("Service is available", health.getDetails().get("velib-stations-api"));
    }

    @Test
    void should_handle_complex_response_structure() {
        ReflectionTestUtils.setField(healthIndicator, "velibApiUrl", "http://localhost:8081");
        Map<String, Object> mockResponse = Map.of(
                "status", "UP",
                "components", Map.of(
                        "diskSpace", Map.of("status", "UP"),
                        "ping", Map.of("status", "UP")
                )
        );
        when(restTemplate.getForObject("http://localhost:8081/actuator/health", Map.class))
                .thenReturn(mockResponse);

        Health health = healthIndicator.health();

        assertEquals(Status.UP, health.getStatus());
        assertEquals("UP", health.getDetails().get("response-status"));
        assertEquals("Service is available", health.getDetails().get("velib-stations-api"));
    }
}
