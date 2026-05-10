package uiktp.team19.web.ml;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.Irr;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uiktp.team19.model.dto.IrrigationPrediction;
import uiktp.team19.model.dto.IrrigationResponse;
import uiktp.team19.service.ml.IrrigationClient;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/ml")
@RequiredArgsConstructor
public class IrrigationController {
    private final IrrigationClient irrigationClient;

    @GetMapping("/latest/{locationId}")
    public IrrigationPrediction getIrrigationLatest(@PathVariable Long locationId) {
        return this.irrigationClient.getLatest(locationId);
    }

    @GetMapping("/all/{locationId}")
    public IrrigationResponse getIrrigationAll(@PathVariable Long locationId) {
        return this.irrigationClient.getAll(locationId);
    }

}
