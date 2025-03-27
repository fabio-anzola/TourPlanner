package at.tw.tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tours")
public class Tour {

    @Id
    @Column(name = "name")
    @Getter
    private String name;

    @Column(name = "description")
    @Getter
    private String description;

    @Column(name = "fromLocation")
    @Getter
    private String fromLocation;

    @Column(name = "toLocation")
    @Getter
    private String toLocation;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourLog> tourLog = new ArrayList<>();

    public Tour(String name, String description, String fromLocation, String toLocation) {
        this.name = name;
        this.description = description;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
    }

    public Tour() {
    }
}
