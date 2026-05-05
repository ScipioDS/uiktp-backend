package uiktp.team19.service.weather_api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestClient;
import uiktp.team19.model.helper.LocationHelper;
import uiktp.team19.model.helper.WeatherAPIResultHelper;

@Service
public class WeatherAPIFetchingService {
    private final RestClient restClient;
    private final ObjectMapper mapper;

    public WeatherAPIFetchingService(@Value("${urls.weather-api-url}") String weatherApiUrl) {
        this.mapper = new ObjectMapper();
        this.restClient = RestClient.builder()
                .baseUrl(weatherApiUrl)
                .build();
    }

    public WeatherAPIResultHelper acquireSoilMoistureHourlyData(LocationHelper helper) throws Exception {
        String json = restClient.get()
                .uri(u -> u.path("/forecast")
                        .queryParam("latitude", helper.getLatitude())
                        .queryParam("longitude", helper.getLongitude())
                        .queryParam("hourly",
                                "soil_moisture_0_to_1cm," +
                                        "soil_moisture_1_to_3cm," +
                                        "soil_moisture_3_to_9cm," +
                                        "soil_moisture_9_to_27cm," +
                                        "soil_moisture_27_to_81cm")
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(String.class);

        JsonNode hourly = mapper.readTree(json).get("hourly");
        JsonNode times  = hourly.get("time");

        WeatherAPIResultHelper weatherAPIResultHelper = new WeatherAPIResultHelper();
        weatherAPIResultHelper.setHourly(hourly);
        weatherAPIResultHelper.setTimes(times);

        return weatherAPIResultHelper;
    };

    public WeatherAPIResultHelper acquireFullHourlyData(LocationHelper helper) throws Exception {
        String json = restClient.get()
                .uri(u -> u.path("/forecast")
                        .queryParam("latitude", helper.getLatitude())
                        .queryParam("longitude", helper.getLongitude())
                        .queryParam("hourly",
                                "soil_temperature_0cm," +
                                        "soil_temperature_6cm," +
                                        "soil_temperature_18cm," +
                                        "soil_temperature_54cm," +
                                        "soil_moisture_0_to_1cm," +
                                        "soil_moisture_1_to_3cm," +
                                        "soil_moisture_3_to_9cm," +
                                        "soil_moisture_9_to_27cm," +
                                        "soil_moisture_27_to_81cm," +
                                        "precipitation_probability," +
                                        "rain," +
                                        "evapotranspiration," +
                                        "et0_fao_evapotranspiration," +
                                        "temperature_2m," +
                                        "relative_humidity_2m," +
                                        "wind_speed_10m," +
                                        "vapour_pressure_deficit," +
                                        "cloud_cover")
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .body(String.class);

        JsonNode hourly = mapper.readTree(json).get("hourly");
        JsonNode times  = hourly.get("time");

        WeatherAPIResultHelper weatherAPIResultHelper = new WeatherAPIResultHelper();
        weatherAPIResultHelper.setHourly(hourly);
        weatherAPIResultHelper.setTimes(times);

        return weatherAPIResultHelper;
    }
}
