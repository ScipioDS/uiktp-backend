package uiktp.team19.model.dto;

import java.util.Map;

public record IrrigationPrediction(
        Long locationId,
        Long recordId,
        String dateTime,
        String irrigationCategory,
        Double waterAmountMm,
        Map<String, Double> features
) {}