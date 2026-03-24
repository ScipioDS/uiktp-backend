package uiktp.team19.model.weather_api;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "weather_api.soil_moisture_data")
public class SoilMoistureData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "dateTime")
    LocalDateTime dateTime;

    @Column(name = "latitude")
    Float latitude;

    @Column(name = "longitude")
    Float longitude;

    @Column(name = "zeroToOneCm")
    Float zeroToOneCm;

    @Column(name = "oneToThreeCm")
    Float oneToThreeCm;

    @Column(name = "threeToNineCm")
    Float threeToNineCm;

    @Column(name = "nineToTwentySevenCm")
    Float nineToTwentySevenCm;

    @Column(name = "twentySevenToEightyOneCm")
    Float twentySevenToEightyOneCm;
}
