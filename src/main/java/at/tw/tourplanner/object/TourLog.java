package at.tw.tourplanner.object;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class TourLog {
    private final SimpleStringProperty date;
    private final SimpleStringProperty comment;
    private final SimpleStringProperty difficulty; // 1-5
    private final SimpleStringProperty totalDistance;
    private final SimpleStringProperty totalTime;
    private final SimpleStringProperty rating; // 1-5
    private final SimpleStringProperty tourName;

    public TourLog(String date, String comment, int difficulty, int totalDistance, int totalTime, int rating, String tourName) {
        this.date = new SimpleStringProperty(date);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleStringProperty(Integer.toString(difficulty));
        this.totalDistance = new SimpleStringProperty(Integer.toString(totalDistance));
        this.totalTime = new SimpleStringProperty(Integer.toString(totalTime));
        this.rating = new SimpleStringProperty(Integer.toString(rating));
        this.tourName = new SimpleStringProperty(tourName);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public String getDifficulty() {
        return difficulty.get();
    }

    public int getParsedDifficulty() {
        return Integer.parseInt(difficulty.get());
    }

    public SimpleStringProperty difficultyProperty() {
        return difficulty;
    }

    public String getTotalDistance() {
        return totalDistance.get();
    }

    public int getParsedTotalDistance() {
        return Integer.parseInt(totalDistance.get());
    }

    public SimpleStringProperty totalDistanceProperty() {
        return totalDistance;
    }

    public String getTotalTime() {
        return totalTime.get();
    }

    public int getParsedTotalTime() {
        return Integer.parseInt(totalTime.get());
    }

    public SimpleStringProperty totalTimeProperty() {
        return totalTime;
    }

    public String getRating() {
        return rating.get();
    }

    public int getParsedRating() {
        return Integer.parseInt(rating.get());
    }

    public SimpleStringProperty ratingProperty() {
        return rating;
    }

    public String getTourName() {
        return tourName.get();
    }

    public SimpleStringProperty tourNameProperty() {
        return tourName;
    }

    public void setTourName(String name) {
        this.tourName.set(name);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    @Override
    public String toString() {
        return "TourLog{" +
                "date=" + date +
                ", comment=" + comment +
                ", tourName=" + tourName +
                '}';
    }
}
