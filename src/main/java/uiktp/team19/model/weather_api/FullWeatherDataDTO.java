package uiktp.team19.model.weather_api;

import java.time.LocalDateTime;

public record FullWeatherDataDTO(
        LocalDateTime dateTime,
        Float latitude,
        Float longitude,
        Float soilTemperature0cm,
        Float soilTemperature6cm,
        Float soilTemperature18cm,
        Float soilTemperature54cm,
        Float soilMoisture0To1cm,
        Float soilMoisture1To3cm,
        Float soilMoisture3To9cm,
        Float soilMoisture9To27cm,
        Float soilMoisture27To81cm,
        Float precipitationProbability,
        Float rain,
        Float evapotranspiration,
        Float et0FaoEvapotranspiration,
        Float temperature2m,
        Float relativeHumidity2m,
        Float windSpeed10m,
        Float vapourPressureDeficit,
        Float cloudCover
) {
}
