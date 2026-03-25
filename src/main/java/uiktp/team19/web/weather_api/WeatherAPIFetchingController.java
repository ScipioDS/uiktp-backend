package uiktp.team19.web.weather_api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uiktp.team19.model.helper.LocationHelper;
import uiktp.team19.model.helper.WeatherAPIResultHelper;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;
import uiktp.team19.model.weather_api.SoilMoistureData;
import uiktp.team19.model.weather_api.SoilMoistureDataDTO;
import uiktp.team19.service.weather_api.SoilMoistureService;
import uiktp.team19.service.weather_api.WeatherAPIFetchingService;
import uiktp.team19.service.weather_api.WeatherDataService;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherAPIFetchingController {
    private final SoilMoistureService soilMoistureService;
    private final WeatherAPIFetchingService weatherAPIFetchingService;
    private final WeatherDataService weatherDataService;

    @GetMapping("/")
    public List<SoilMoistureData> getEntityData() throws Exception {
        //41.9965 lat
        //21.4314 long
        LocationHelper locationHelper = new LocationHelper();
        locationHelper.setLatitude((float) 41.9965);
        locationHelper.setLongitude((float) 22.4314);
        return soilMoistureService.acquireHourlyDataEntity(locationHelper);
    }

    @GetMapping("/dto")
    public List<SoilMoistureDataDTO> getDTOData() throws Exception {
        //41.9965 lat
        //21.4314 long
        LocationHelper locationHelper = new LocationHelper();
        locationHelper.setLatitude((float) 41.9965);
        locationHelper.setLongitude((float) 22.4314);
        return soilMoistureService.acquireHourlyDataDTO(locationHelper);
    }

    @GetMapping("/full")
    public List<FullWeatherDataDTO> getFullWeatherData() throws Exception {
        LocationHelper locationHelper = new LocationHelper();
        locationHelper.setLatitude((float) 41.9965);
        locationHelper.setLongitude((float) 22.4314);

        return weatherDataService.acquireFullHourlyDataDTO(locationHelper);
    }
}
