package uiktp.team19.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.auth.User;
import uiktp.team19.model.helper.LocationCreationHelper;
import uiktp.team19.model.location.Location;
import uiktp.team19.model.weather_api.FullWeatherDataDTO;
import uiktp.team19.repository.LocationRepository;
import uiktp.team19.service.auth.UserService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserService userService;

    public List<Location> findAll() {
        return locationRepository.findAll();
    }

    public List<Location> findAllPredefined() {
        return locationRepository.findAllByIsPredefined(true);
    }

    public Location findById(Long id) {
        return locationRepository.findById(id).orElseThrow();
    }

    public Location save(LocationCreationHelper helper, Location location) {
        if (helper.getUserId() != null) {
            location.setUser(this.userService.loadJustUserById(helper.getUserId()));
        }

        if (helper.getLatitude() != null) {
            location.setLatitude(helper.getLatitude());
        }

        if (helper.getLongitude() != null) {
            location.setLongitude(helper.getLongitude());
        }

        if (helper.getName() != null) {
            location.setName(helper.getName());
        }

        location.setIsPredefined(false);

        return locationRepository.save(location);
    }

    public Location create(LocationCreationHelper helper) {
        Location location = new Location();
        return save(helper, location);
    }

    public Location update(LocationCreationHelper helper) {
        Location location = findById(helper.getId());
        return save(helper, location);
    }

    public void delete(Long id) {
        Location location = this.findById(id);
        this.locationRepository.delete(location);
    }

    public List<Location> getAllByUserId(UUID uuid) {
        User user = this.userService.loadJustUserById(uuid);
        return this.locationRepository.findAllByUser(user);
    }
}
