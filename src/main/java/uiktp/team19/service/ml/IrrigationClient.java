package uiktp.team19.service.ml;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uiktp.team19.model.dto.IrrigationPrediction;
import uiktp.team19.model.dto.IrrigationResponse;

@Service
public class IrrigationClient {

    private final RestTemplate restTemplate;

    @Value("${irrigation.api.base-url:http://localhost:8000}")
    private String baseUrl;

    public IrrigationClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /** Get the latest irrigation prediction for one location */
    public IrrigationPrediction getLatest(Long locationId) {
        String url = baseUrl + "/predict/" + locationId + "/latest";
        return restTemplate.getForObject(url, IrrigationPrediction.class);
    }

    /** Get all predictions (one per weather record) for a location */
    public IrrigationResponse getAll(Long locationId) {
        String url = baseUrl + "/predict/" + locationId;
        return restTemplate.getForObject(url, IrrigationResponse.class);
    }
}