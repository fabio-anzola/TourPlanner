package at.tw.tourplanner.object;

import javafx.beans.property.SimpleStringProperty;

/**
 * Object that storages log information
 */
public class TourLog {
    /**
     * The tour log date
     */
    private final SimpleStringProperty date;

    /**
     * The tour log comment
     */
    private final SimpleStringProperty comment;

    /**
     * The tour log difficulty
     */
    private final SimpleStringProperty difficulty; // 1-5

    /**
     * The tour log total distance
     */
    private final SimpleStringProperty totalDistance;

    /**
     * The tour log total time
     */
    private final SimpleStringProperty totalTime;

    /**
     * The tour log rating
     */
    private final SimpleStringProperty rating; // 1-5

    /**
     * The tour log name for associated tour
     */
    private final SimpleStringProperty tourName;

    /**
     * Constructor for full Tour Log
     *
     * @param date the date
     * @param comment the comment
     * @param difficulty the difficulty
     * @param totalDistance the total distance
     * @param totalTime the total time
     * @param rating the rating
     * @param tourName the associated tour
     */
    public TourLog(String date, String comment, int difficulty, int totalDistance, int totalTime, int rating, String tourName) {
        this.date = new SimpleStringProperty(date);
        this.comment = new SimpleStringProperty(comment);
        this.difficulty = new SimpleStringProperty(Integer.toString(difficulty));
        this.totalDistance = new SimpleStringProperty(Integer.toString(totalDistance));
        this.totalTime = new SimpleStringProperty(Integer.toString(totalTime));
        this.rating = new SimpleStringProperty(Integer.toString(rating));
        this.tourName = new SimpleStringProperty(tourName);
    }

    /**
     * Getter for date
     *
     * @return the date
     */
    public String getDate() {
        return date.get();
    }

    /**
     * Getter for date property
     *
     * @return the date property
     */
    public SimpleStringProperty dateProperty() {
        return date;
    }

    /**
     * Getter for comment
     *
     * @return the comment
     */
    public String getComment() {
        return comment.get();
    }

    /**
     * Getter for comment property
     *
     * @return the comment property
     */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    /**
     * Getter for difficulty
     *
     * @return the difficulty
     */
    public String getDifficulty() {

        return difficulty.get();
    }

    /**
     * Getter for difficulty property
     *
     * @return the difficulty property
     */
    public SimpleStringProperty difficultyProperty() {
        return difficulty;
    }

    /**
     * Getter for difficulty in a parsed format
     *
     * @return the difficulty as Integer
     */
    public int getParsedDifficulty() {
        return Integer.parseInt(difficulty.get());
    }

    /**
     * Getter for total distance
     *
     * @return the total distance
     */
    public String getTotalDistance() {
        return totalDistance.get();
    }

    /**
     * Getter for the total distance property
     *
     * @return the total dinstance property
     */
    public SimpleStringProperty totalDistanceProperty() {
        return totalDistance;
    }

    /**
     * Getter for total distance in a parsed format
     *
     * @return the total distance as Integer
     */
    public int getParsedTotalDistance() {
        return Integer.parseInt(totalDistance.get());
    }

    /**
     * Getter for total time
     *
     * @return the total time
     */
    public String getTotalTime() {
        return totalTime.get();
    }

    /**
     * Getter for total time property
     *
     * @return the total time property
     */
    public SimpleStringProperty totalTimeProperty() {
        return totalTime;
    }

    /**
     * Getter for total time in a parsed format
     *
     * @return the total time as Integer
     */
    public int getParsedTotalTime() {
        return Integer.parseInt(totalTime.get());
    }

    /**
     * Getter for rating
     *
     * @return the rating
     */
    public String getRating() {
        return rating.get();
    }

    /**
     * Getter for rating property
     *
     * @return the rating property
     */
    public SimpleStringProperty ratingProperty() {
        return rating;
    }

    /**
     * Getter for rating in a parsed format
     *
     * @return the rating as Integer
     */
    public int getParsedRating() {
        return Integer.parseInt(rating.get());
    }

    /**
     * Getter for associated tour name
     *
     * @return the tour name
     */
    public String getTourName() {
        return tourName.get();
    }

    /**
     * Getter for associated tour name property
     *
     * @return the tour name property
     */
    public SimpleStringProperty tourNameProperty() {
        return tourName;
    }

    /**
     * Setter for associated tour name
     *
     * @param name the tour name
     */
    public void setTourName(String name) {
        this.tourName.set(name);
    }

    /**
     * To String function for logs - only shows minimal information
     *
     * @return an abbreviated form of the object
     */
    @Override
    public String toString() {
        return "TourLog{" +
                "date=" + date +
                ", comment=" + comment +
                ", tourName=" + tourName +
                '}';
    }
}
