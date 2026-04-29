package uiktp.team19.model.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import uiktp.team19.model.auth.User;

@Entity
@Data
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToOne
    private User user;

    @Column(name = "is_predefined")
    private Boolean isPredefined;
}
