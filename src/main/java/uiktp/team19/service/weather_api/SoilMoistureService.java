package uiktp.team19.service.weather_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.helper.LocationHelper;
import uiktp.team19.model.helper.WeatherAPIResultHelper;
import uiktp.team19.model.weather_api.SoilMoistureData;
import uiktp.team19.model.weather_api.SoilMoistureDataDTO;
import uiktp.team19.repository.SoilMoistureRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoilMoistureService {
    private final SoilMoistureRepository soilMoistureRepository;
    private final WeatherAPIFetchingService weatherAPIFetchingService;

    public List<SoilMoistureData> acquireHourlyDataEntity(LocationHelper helper) throws Exception {
        WeatherAPIResultHelper weatherAPIResultHelper = weatherAPIFetchingService.acquireSoilMoistureHourlyData(helper);
        JsonNode hourly = weatherAPIResultHelper.getHourly();
        JsonNode times = weatherAPIResultHelper.getTimes();

        List<SoilMoistureData> results = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            SoilMoistureData entry = new SoilMoistureData();

            entry.setLatitude(helper.getLatitude());
            entry.setLongitude(helper.getLongitude());

            entry.setDateTime(
                    LocalDateTime.parse(times.get(i).asText(),
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

            entry.setZeroToOneCm(
                    toFloat(hourly.get("soil_moisture_0_to_1cm").get(i)));
            entry.setOneToThreeCm(
                    toFloat(hourly.get("soil_moisture_1_to_3cm").get(i)));
            entry.setThreeToNineCm(
                    toFloat(hourly.get("soil_moisture_3_to_9cm").get(i)));
            entry.setNineToTwentySevenCm(
                    toFloat(hourly.get("soil_moisture_9_to_27cm").get(i)));
            entry.setTwentySevenToEightyOneCm(
                    toFloat(hourly.get("soil_moisture_27_to_81cm").get(i)));

            results.add(entry);
        }

        soilMoistureRepository.saveAll(results);

        return results;
    }

    public List<SoilMoistureDataDTO> acquireHourlyDataDTO(LocationHelper helper) throws Exception {
        WeatherAPIResultHelper weatherAPIResultHelper = weatherAPIFetchingService.acquireSoilMoistureHourlyData(helper);
        JsonNode hourly = weatherAPIResultHelper.getHourly();
        JsonNode times = weatherAPIResultHelper.getTimes();

        List<SoilMoistureDataDTO> results = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            SoilMoistureDataDTO entry = new SoilMoistureDataDTO(
                    LocalDateTime.parse(times.get(i).asText(),DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    helper.getLatitude(),
                    helper.getLongitude(),
                    toFloat(hourly.get("soil_moisture_0_to_1cm").get(i)),
                    toFloat(hourly.get("soil_moisture_1_to_3cm").get(i)),
                    toFloat(hourly.get("soil_moisture_3_to_9cm").get(i)),
                    toFloat(hourly.get("soil_moisture_9_to_27cm").get(i)),
                    toFloat(hourly.get("soil_moisture_27_to_81cm").get(i))
            );

            results.add(entry);
        }

        return results;
    }

    private Float toFloat(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.floatValue();
    }
}
