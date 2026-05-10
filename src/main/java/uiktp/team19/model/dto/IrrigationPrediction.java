package uiktp.team19.model.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public record IrrigationPrediction(
        @JsonProperty("location_id")    Long locationId,
        @JsonProperty("record_id")      Long recordId,
        @JsonProperty("date_time")      String dateTime,
        @JsonProperty("irrigation_category") String irrigationCategory,
        @JsonProperty("water_amount_mm") Double waterAmountMm,
        @JsonProperty("features")       Map<String, Double> features
) {}