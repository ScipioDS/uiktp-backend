package uiktp.team19.model.dto;

import java.util.List;

public record IrrigationResponse(
        Long locationId,
        List<IrrigationPrediction> predictions,
        Integer totalRecords
) {}