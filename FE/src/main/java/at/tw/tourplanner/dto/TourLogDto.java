package at.tw.tourplanner.dto;

import lombok.Getter;

import java.sql.Date;

@Getter
public class TourLogDto {
    public long id;
    public Date date;
    public String comment;
    public int difficulty;
    public int totalDistance;
    public int totalTime;
    public int rating;
    public TourDto tour;
}