package uiktp.team19.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record IrrigationResponse(
        @JsonProperty("location_id")    Long locationId,
        @JsonProperty("predictions")    List<IrrigationPrediction> predictions,
        @JsonProperty("total_records")  Integer totalRecords
) {}