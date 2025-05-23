package at.tw.tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

/**
 * Entity class representing a TourLog.
 */
@Entity
@Table(name = "tourlog")
public class TourLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Column(name = "date")
    @Getter
    @Setter
    private Date date;

    @Column(name = "comment")
    @Getter
    @Setter
    private String comment;

    @Column(name = "difficulty")
    @Getter
    @Setter
    private int difficulty;

    @Column(name = "totalDistance")
    @Getter
    @Setter
    private int totalDistance;

    @Column(name = "totalTime")
    @Getter
    @Setter
    private int totalTime;

    @Column(name = "rating")
    @Getter
    @Setter
    private int rating;

    @ManyToOne
    @JoinColumn(name = "tourName")
    @Getter
    @Setter
    private Tour tour;

    /**
     * Constructs a TourLog with specified attributes.
     *
     * @param date the date of the log
     * @param comment user comment
     * @param difficulty difficulty rating
     * @param totalDistance distance covered
     * @param totalTime time taken
     * @param rating overall experience rating
     */
    public TourLog(Date date, String comment, int difficulty, int totalDistance, int totalTime, int rating) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.rating = rating;
    }

    /**
     * Default constructor.
     */
    public TourLog() {
    }
}
