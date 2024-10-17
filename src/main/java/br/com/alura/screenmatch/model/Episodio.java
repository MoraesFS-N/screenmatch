package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {


    private Integer season;
    private String title;
    private Integer epNumber;
    private Double rating;
    private LocalDate released;

    public Episodio(Integer seasonNumber, DadosEpisodio epData) {
        this.season = seasonNumber;
        this.title = epData.title();
        this.epNumber = epData.number();

        try {
            this.rating = Double.valueOf(epData.rating());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }

        try {
            this.released = LocalDate.parse(epData.released());
        } catch (DateTimeParseException ex) {
            this.released = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEpNumber() {
        return epNumber;
    }

    public void setEpNumber(Integer epNumber) {
        this.epNumber = epNumber;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    @Override
    public String toString() {
        return "Episodio{" +
                "season=" + season +
                ", title='" + title + '\'' +
                ", epNumber=" + epNumber +
                ", rating=" + rating +
                ", released=" + released +
                '}';
    }
}
