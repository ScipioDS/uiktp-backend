package uiktp.team19.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uiktp.team19.model.weather_api.SoilMoistureData;

public interface SoilMoistureRepository extends JpaRepository<SoilMoistureData, Long> {
}
