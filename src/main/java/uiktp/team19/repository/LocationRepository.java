package uiktp.team19.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uiktp.team19.model.auth.User;
import uiktp.team19.model.location.Location;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByUser(User user);

    List<Location> findAllByIsPredefined(Boolean isPredefined);
}
