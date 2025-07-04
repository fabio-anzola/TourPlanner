package at.tw.tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class representing a Tour.
 */
@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    @Column(name = "fromLocation")
    @Getter
    @Setter
    private String fromLocation;

    @Column(name = "toLocation")
    @Getter
    @Setter
    private String toLocation;

    @Column(name = "transportType")
    @Getter
    @Setter
    private String transportType;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourLog> tourLog = new ArrayList<>();

    /**
     * Constructs a Tour with specified properties.
     *
     * @param name the name of the tour
     * @param description the tour description
     * @param fromLocation the starting location of the tour
     * @param toLocation the ending location of the tour
     * @param transportType the transport type used for the tour
     */
    public Tour(String name, String description, String fromLocation, String toLocation, String transportType) {
        this.name = name;
        this.description = description;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.transportType = transportType;
    }

    /**
     * Default constructor.
     */
    public Tour() {
    }
}
