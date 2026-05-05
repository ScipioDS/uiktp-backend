package uiktp.team19.model.helper;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class WeatherAPIResultHelper {
    JsonNode hourly;
    JsonNode times;
}
