package at.tw.tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
