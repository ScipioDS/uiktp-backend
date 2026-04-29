package uiktp.team19.model.helper;

import lombok.Data;

import java.util.UUID;

@Data
public class LocationCreationHelper {
    Long id;
    Double latitude;
    Double longitude;
    String name;
    UUID userId;
}
