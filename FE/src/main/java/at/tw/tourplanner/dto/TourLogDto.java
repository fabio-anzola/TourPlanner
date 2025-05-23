package at.tw.tourplanner.dto;

import lombok.Getter;
import java.sql.Date;

/**
 * Data transfer object for a tour log.
 */
@Getter
public class TourLogDto {
    /** The ID of the tour log. */
    public long id;

    /** The date of the log entry. */
    public Date date;

    /** The comment for the tour log. */
    public String comment;

    /** The difficulty rating. */
    public int difficulty;

    /** The total distance of the tour. */
    public int totalDistance;

    /** The total time of the tour. */
    public int totalTime;

    /** The overall rating of the tour. */
    public int rating;

    /** The tour associated with this log. */
    public TourDto tour;
}