package uiktp.team19.web.location;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uiktp.team19.model.helper.LocationCreationHelper;
import uiktp.team19.model.location.Location;
import uiktp.team19.service.location.LocationService;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/{id}")
    public Location getLocation(@PathVariable long id) {
        return locationService.findById(id);
    }

    @PostMapping("/create")
    public Location createLocation(@RequestBody LocationCreationHelper location) {
        return this.locationService.create(location);
    }

    @PostMapping("/update")
    public Location updateLocation(@RequestBody LocationCreationHelper location) {
        return this.locationService.update(location);
    }

    @GetMapping("/user/{uuid}")
    public List<Location> getLocationByUUID(@PathVariable UUID uuid) {
        return this.locationService.getAllByUserId(uuid);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable long id) {
        this.locationService.delete(id);
    }
}
