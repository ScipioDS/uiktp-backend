package uiktp.team19.web.location;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uiktp.team19.model.location.Location;
import uiktp.team19.service.location.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/public/location")
@RequiredArgsConstructor
public class LocationPublicController {
    private final LocationService locationService;

    @GetMapping("/")
    public List<Location> getLocations() {
        return this.locationService.findAllPredefined();
    }
}
