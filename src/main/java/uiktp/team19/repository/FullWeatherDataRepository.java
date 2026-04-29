package uiktp.team19.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uiktp.team19.model.weather_api.FullWeatherData;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;

public interface FullWeatherDataRepository extends JpaRepository<FullWeatherData, Long> {
}
