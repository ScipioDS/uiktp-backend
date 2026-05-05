package uiktp.team19.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uiktp.team19.model.location.Location;
import uiktp.team19.model.weather_api.FullWeatherData;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FullWeatherDataRepository extends JpaRepository<FullWeatherData, Long> {
    Optional<FullWeatherData> findByLocationAndDateTime(Location location, LocalDateTime dateTime);
    List<FullWeatherData> findAllByLocation(Location location);
}