package uiktp.team19.model.weather_api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import uiktp.team19.model.location.Location;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "weather_data")
@NoArgsConstructor
public class FullWeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateTime;

    private Float soilTemperature0cm;
    private Float soilTemperature6cm;
    private Float soilTemperature18cm;
    private Float soilTemperature54cm;

    private Float soilMoisture0To1cm;
    private Float soilMoisture1To3cm;
    private Float soilMoisture3To9cm;
    private Float soilMoisture9To27cm;
    private Float soilMoisture27To81cm;

    private Float precipitationProbability;
    private Float rain;
    private Float evapotranspiration;
    private Float et0FaoEvapotranspiration;

    private Float temperature2m;
    private Float relativeHumidity2m;
    private Float windSpeed10m;
    private Float vapourPressureDeficit;
    private Float cloudCover;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnore
    private Location location;
}