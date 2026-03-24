package uiktp.team19.service.weather_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.helper.LocationHelper;
import uiktp.team19.model.helper.WeatherAPIResultHelper;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherDataService {
    private final WeatherAPIFetchingService weatherAPIFetchingService;

    public List<FullWeatherDataDTO> acquireFullHourlyDataDTO(LocationHelper helper) throws Exception {
        WeatherAPIResultHelper weatherAPIResultHelper = weatherAPIFetchingService.acquireFullHourlyData(helper);
        JsonNode hourly = weatherAPIResultHelper.getHourly();
        JsonNode times = weatherAPIResultHelper.getTimes();

        List<FullWeatherDataDTO> results = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            FullWeatherDataDTO entry = new FullWeatherDataDTO(
                    LocalDateTime.parse(times.get(i).asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    helper.getLatitude(),
                    helper.getLongitude(),
                    toFloat(hourly.get("soil_temperature_0cm").get(i)),
                    toFloat(hourly.get("soil_temperature_6cm").get(i)),
                    toFloat(hourly.get("soil_temperature_18cm").get(i)),
                    toFloat(hourly.get("soil_temperature_54cm").get(i)),
                    toFloat(hourly.get("soil_moisture_0_to_1cm").get(i)),
                    toFloat(hourly.get("soil_moisture_1_to_3cm").get(i)),
                    toFloat(hourly.get("soil_moisture_3_to_9cm").get(i)),
                    toFloat(hourly.get("soil_moisture_9_to_27cm").get(i)),
                    toFloat(hourly.get("soil_moisture_27_to_81cm").get(i)),
                    toFloat(hourly.get("precipitation_probability").get(i)),
                    toFloat(hourly.get("rain").get(i)),
                    toFloat(hourly.get("evapotranspiration").get(i)),
                    toFloat(hourly.get("et0_fao_evapotranspiration").get(i)),
                    toFloat(hourly.get("temperature_2m").get(i)),
                    toFloat(hourly.get("relative_humidity_2m").get(i)),
                    toFloat(hourly.get("wind_speed_10m").get(i)),
                    toFloat(hourly.get("vapour_pressure_deficit").get(i)),
                    toFloat(hourly.get("cloud_cover").get(i))
            );

            results.add(entry);
        }

        return results;
    }


    private Float toFloat(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.floatValue();
    }
}
