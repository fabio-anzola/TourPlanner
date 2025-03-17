package at.tw.tourplanner.object;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.security.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class TourLog {
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleStringProperty comment;
    private final SimpleIntegerProperty difficulty; // 1-5
    private final SimpleIntegerProperty totalDistance;
    private final SimpleIntegerProperty totalTime;
    private final SimpleIntegerProperty rating; // 1-5
    private final SimpleStringProperty tourName;

    public TourLog(LocalDate date, String comment, int difficulty, int totalDistance, int totalTime, int rating, String tourName) {
        this.date = new SimpleObjectProperty<>(date);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleIntegerProperty(difficulty);
        this.totalDistance = new SimpleIntegerProperty(totalDistance);
        this.totalTime = new SimpleIntegerProperty(totalTime);
        this.rating = new SimpleIntegerProperty(rating);
        this.tourName = new SimpleStringProperty(tourName);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public int getDifficulty() {
        return difficulty.get();
    }

    public SimpleIntegerProperty difficultyProperty() {
        return difficulty;
    }

    public int getTotalDistance() {
        return totalDistance.get();
    }

    public SimpleIntegerProperty totalDistanceProperty() {
        return totalDistance;
    }

    public int getTotalTime() {
        return totalTime.get();
    }

    public SimpleIntegerProperty totalTimeProperty() {
        return totalTime;
    }

    public int getRating() {
        return rating.get();
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    public String getTourName() {
        return tourName.get();
    }

    public SimpleStringProperty tourNameProperty() {
        return tourName;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }
}
