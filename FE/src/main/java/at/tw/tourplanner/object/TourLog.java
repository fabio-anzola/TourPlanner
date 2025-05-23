package at.tw.tourplanner.object;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Stores tour log information.
 */
public class TourLog {

    /** The tour log id. */
    @Getter
    @Setter
    private int id;

    /** The tour log date. */
    private final SimpleStringProperty date;

    /** The tour log comment. */
    private final SimpleStringProperty comment;

    /** The tour log difficulty. */
    private final SimpleStringProperty difficulty;

    /** The tour log total distance. */
    private final SimpleStringProperty totalDistance;

    /** The tour log total time. */
    private final SimpleStringProperty totalTime;

    /** The tour log rating. */
    private final SimpleStringProperty rating;

    /** The tour log name for the associated tour. */
    private final SimpleStringProperty tourName;

    /**
     * Creates a TourLog with all fields.
     */
    public TourLog(int id, String date, String comment, int difficulty, int totalDistance, int totalTime, int rating, String tourName) {
        this.id = id;
        this.date = new SimpleStringProperty(date);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleStringProperty(Integer.toString(difficulty));
        this.totalDistance = new SimpleStringProperty(Integer.toString(totalDistance));
        this.totalTime = new SimpleStringProperty(Integer.toString(totalTime));
        this.rating = new SimpleStringProperty(Integer.toString(rating));
        this.tourName = new SimpleStringProperty(tourName);
    }

    /** Returns the date. */
    public String getDate() {
        return date.get();
    }

    /** Returns the date property. */
    public SimpleStringProperty dateProperty() {
        return date;
    }

    /** Returns the comment. */
    public String getComment() {
        return comment.get();
    }

    /** Returns the comment property. */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    /** Returns the difficulty. */
    public String getDifficulty() {
        return difficulty.get();
    }

    /** Returns the difficulty property. */
    public SimpleStringProperty difficultyProperty() {
        return difficulty;
    }

    /** Returns the difficulty as integer. */
    public int getParsedDifficulty() {
        return Integer.parseInt(difficulty.get());
    }

    /** Returns the total distance. */
    public String getTotalDistance() {
        return totalDistance.get();
    }

    /** Returns the total distance property. */
    public SimpleStringProperty totalDistanceProperty() {
        return totalDistance;
    }

    /** Returns the total distance as integer. */
    public int getParsedTotalDistance() {
        return Integer.parseInt(totalDistance.get());
    }

    /** Returns the total time. */
    public String getTotalTime() {
        return totalTime.get();
    }

    /** Returns the total time property. */
    public SimpleStringProperty totalTimeProperty() {
        return totalTime;
    }

    /** Returns the total time as integer. */
    public int getParsedTotalTime() {
        return Integer.parseInt(totalTime.get());
    }

    /** Returns the rating. */
    public String getRating() {
        return rating.get();
    }

    /** Returns the rating property. */
    public SimpleStringProperty ratingProperty() {
        return rating;
    }

    /** Returns the rating as integer. */
    public int getParsedRating() {
        return Integer.parseInt(rating.get());
    }

    /** Returns the associated tour name. */
    public String getTourName() {
        return tourName.get();
    }

    /** Returns the associated tour name property. */
    public SimpleStringProperty tourNameProperty() {
        return tourName;
    }

    /** Sets the associated tour name. */
    public void setTourName(String name) {
        this.tourName.set(name);
    }

    /** Returns a minimal string representation. */
    @Override
    public String toString() {
        return "TourLog{" +
                "date=" + date +
                ", comment=" + comment +
                ", tourName=" + tourName +
                '}';
    }
}