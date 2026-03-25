package uiktp.team19.model.weather_api;

import java.time.LocalDateTime;

public record SoilMoistureDataDTO (
        LocalDateTime dateTime,
        Float latitude,
        Float longitude,
        Float zeroToOneCm,
        Float oneToThreeCm,
        Float threeToNineCm,
        Float nineToTwentySevenCm,
        Float twentySevenToEightyOneCm
) {}
