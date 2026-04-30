package uiktp.team19.service.weather_api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.helper.LocationHelper;
import uiktp.team19.model.helper.WeatherAPIResultHelper;
import uiktp.team19.model.location.Location;
import uiktp.team19.model.weather_api.FullWeatherData;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;
import uiktp.team19.repository.FullWeatherDataRepository;
import uiktp.team19.service.location.LocationService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeatherDataService {
    private final FullWeatherDataRepository fullWeatherDataRepository;
    private final WeatherAPIFetchingService weatherAPIFetchingService;
    private final LocationService locationService;

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

    public List<FullWeatherDataDTO> acquireFullHourlyDataDTO(Long locationId) throws Exception {
        Location location = locationService.findById(locationId);

        LocationHelper helper = new LocationHelper();
        helper.setLatitude(location.getLatitude().floatValue());
        helper.setLongitude(location.getLongitude().floatValue());

        WeatherAPIResultHelper weatherAPIResultHelper = weatherAPIFetchingService.acquireFullHourlyData(helper);
        JsonNode hourly = weatherAPIResultHelper.getHourly();
        JsonNode times = weatherAPIResultHelper.getTimes();

        Map<LocalDateTime, FullWeatherData> existing = fullWeatherDataRepository
                .findAllByLocation(location)
                .stream()
                .collect(Collectors.toMap(FullWeatherData::getDateTime, e -> e));

        List<FullWeatherData> toSave = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            LocalDateTime dateTime = LocalDateTime.parse(times.get(i).asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            FullWeatherData entity = existing.getOrDefault(dateTime, new FullWeatherData());

            entity.setLocation(location);
            entity.setDateTime(dateTime);
            entity.setSoilTemperature0cm(toFloat(hourly.get("soil_temperature_0cm").get(i)));
            entity.setSoilTemperature6cm(toFloat(hourly.get("soil_temperature_6cm").get(i)));
            entity.setSoilTemperature18cm(toFloat(hourly.get("soil_temperature_18cm").get(i)));
            entity.setSoilTemperature54cm(toFloat(hourly.get("soil_temperature_54cm").get(i)));
            entity.setSoilMoisture0To1cm(toFloat(hourly.get("soil_moisture_0_to_1cm").get(i)));
            entity.setSoilMoisture1To3cm(toFloat(hourly.get("soil_moisture_1_to_3cm").get(i)));
            entity.setSoilMoisture3To9cm(toFloat(hourly.get("soil_moisture_3_to_9cm").get(i)));
            entity.setSoilMoisture9To27cm(toFloat(hourly.get("soil_moisture_9_to_27cm").get(i)));
            entity.setSoilMoisture27To81cm(toFloat(hourly.get("soil_moisture_27_to_81cm").get(i)));
            entity.setPrecipitationProbability(toFloat(hourly.get("precipitation_probability").get(i)));
            entity.setRain(toFloat(hourly.get("rain").get(i)));
            entity.setEvapotranspiration(toFloat(hourly.get("evapotranspiration").get(i)));
            entity.setEt0FaoEvapotranspiration(toFloat(hourly.get("et0_fao_evapotranspiration").get(i)));
            entity.setTemperature2m(toFloat(hourly.get("temperature_2m").get(i)));
            entity.setRelativeHumidity2m(toFloat(hourly.get("relative_humidity_2m").get(i)));
            entity.setWindSpeed10m(toFloat(hourly.get("wind_speed_10m").get(i)));
            entity.setVapourPressureDeficit(toFloat(hourly.get("vapour_pressure_deficit").get(i)));
            entity.setCloudCover(toFloat(hourly.get("cloud_cover").get(i)));

            toSave.add(entity);
        }

        fullWeatherDataRepository.saveAll(toSave);

        return toSave.stream().map(e -> new FullWeatherDataDTO(
                e.getDateTime(),
                location.getLatitude().floatValue(),
                location.getLongitude().floatValue(),
                e.getSoilTemperature0cm(),
                e.getSoilTemperature6cm(),
                e.getSoilTemperature18cm(),
                e.getSoilTemperature54cm(),
                e.getSoilMoisture0To1cm(),
                e.getSoilMoisture1To3cm(),
                e.getSoilMoisture3To9cm(),
                e.getSoilMoisture9To27cm(),
                e.getSoilMoisture27To81cm(),
                e.getPrecipitationProbability(),
                e.getRain(),
                e.getEvapotranspiration(),
                e.getEt0FaoEvapotranspiration(),
                e.getTemperature2m(),
                e.getRelativeHumidity2m(),
                e.getWindSpeed10m(),
                e.getVapourPressureDeficit(),
                e.getCloudCover()
        )).toList();
    }

    public List<FullWeatherData> saveWeatherData(List<FullWeatherDataDTO> dtos) {
        List<FullWeatherData> entities = dtos.stream()
                .map(this::toEntity)
                .toList();

        return fullWeatherDataRepository.saveAll(entities);
    }

    private FullWeatherData toEntity(FullWeatherDataDTO dto) {
        FullWeatherData entity = new FullWeatherData();
        entity.setDateTime(dto.dateTime());
        entity.setSoilTemperature0cm(dto.soilTemperature0cm());
        entity.setSoilTemperature6cm(dto.soilTemperature6cm());
        entity.setSoilTemperature18cm(dto.soilTemperature18cm());
        entity.setSoilTemperature54cm(dto.soilTemperature54cm());
        entity.setSoilMoisture0To1cm(dto.soilMoisture0To1cm());
        entity.setSoilMoisture1To3cm(dto.soilMoisture1To3cm());
        entity.setSoilMoisture3To9cm(dto.soilMoisture3To9cm());
        entity.setSoilMoisture9To27cm(dto.soilMoisture9To27cm());
        entity.setSoilMoisture27To81cm(dto.soilMoisture27To81cm());
        entity.setPrecipitationProbability(dto.precipitationProbability());
        entity.setRain(dto.rain());
        entity.setEvapotranspiration(dto.evapotranspiration());
        entity.setEt0FaoEvapotranspiration(dto.et0FaoEvapotranspiration());
        entity.setTemperature2m(dto.temperature2m());
        entity.setRelativeHumidity2m(dto.relativeHumidity2m());
        entity.setWindSpeed10m(dto.windSpeed10m());
        entity.setVapourPressureDeficit(dto.vapourPressureDeficit());
        entity.setCloudCover(dto.cloudCover());
        return entity;
    }

    private FullWeatherDataDTO toDto(FullWeatherData data) {
        return new FullWeatherDataDTO(
                data.getDateTime(),
                data.getLocation().getLatitude().floatValue(),
                data.getLocation().getLongitude().floatValue(),
                data.getSoilTemperature0cm(),
                data.getSoilTemperature6cm(),
                data.getSoilTemperature18cm(),
                data.getSoilTemperature54cm(),
                data.getSoilMoisture0To1cm(),
                data.getSoilMoisture1To3cm(),
                data.getSoilMoisture3To9cm(),
                data.getSoilMoisture9To27cm(),
                data.getSoilMoisture27To81cm(),
                data.getPrecipitationProbability(),
                data.getRain(),
                data.getEvapotranspiration(),
                data.getEt0FaoEvapotranspiration(),
                data.getTemperature2m(),
                data.getRelativeHumidity2m(),
                data.getWindSpeed10m(),
                data.getVapourPressureDeficit(),
                data.getCloudCover()
        );
    }

    private Float toFloat(JsonNode node) {
        return (node == null || node.isNull()) ? null : node.floatValue();
    }

    public List<FullWeatherDataDTO> getFullWeatherDataFromDB(Long locationId) {
        Location location = this.locationService.findById(locationId);
        LocalDateTime cutoff = LocalDateTime.now().minusDays(30);
        return fullWeatherDataRepository.findAllByLocation(location)
                .stream()
                .filter(e -> !e.getDateTime().isBefore(cutoff))
                .map(this::toDto)
                .toList();
    }
}