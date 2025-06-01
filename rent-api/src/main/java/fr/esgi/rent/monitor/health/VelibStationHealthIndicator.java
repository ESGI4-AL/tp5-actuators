package fr.esgi.rent.monitor.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class VelibStationHealthIndicator implements HealthIndicator {

    private final RestTemplate restTemplate;

    @Value("${velib.station.api.url:http://localhost:8081}")
    private String velibApiUrl;

    public VelibStationHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        try {
            String healthUrl = velibApiUrl + "/actuator/health";
            Map response = restTemplate.getForObject(healthUrl, Map.class);

            if ("UP".equals(response.get("status"))) {
                return Health.up()
                        .withDetail("velib-stations-api", "Service is available")
                        .withDetail("url", healthUrl)
                        .withDetail("response-status", response.get("status"))
                        .build();
            } else {
                return Health.down()
                        .withDetail("velib-stations-api", "Service returned non-UP status")
                        .withDetail("url", healthUrl)
                        .withDetail("response-status", response.get("status"))
                        .withDetail("full-response", response)
                        .build();
            }
        } catch (Exception e) {
            return Health.down()
                    .withDetail("velib-stations-api", "Service is unavailable")
                    .withDetail("url", velibApiUrl + "/actuator/health")
                    .withDetail("error", e.getMessage())
                    .withDetail("error-type", e.getClass().getSimpleName())
                    .build();
        }
    }
}
