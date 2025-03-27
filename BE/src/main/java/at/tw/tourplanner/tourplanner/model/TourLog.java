package at.tw.tourplanner.tourplanner.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Date;

@Entity
@Table(name = "tourlog")
public class TourLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date")
    @Getter
    private Date date;

    @Column(name = "comment")
    @Getter
    private String comment;

    @Column(name = "difficulty")
    @Getter
    private int difficulty;

    @Column(name = "totalDistance")
    @Getter
    private int totalDistance;

    @Column(name = "totalTime")
    @Getter
    private int totalTime;

    @Column(name = "rating")
    @Getter
    private int rating;

    @ManyToOne
    @JoinColumn(name = "tourName")
    @Getter
    private Tour tour;

    public TourLog(Date date, String comment, int difficulty, int totalDistance, int totalTime, int rating) {
        this.date = date;
        this.comment = comment;
        this.difficulty = difficulty;
        this.totalDistance = totalDistance;
        this.totalTime = totalTime;
        this.rating = rating;
    }

    public TourLog() {
    }
}
