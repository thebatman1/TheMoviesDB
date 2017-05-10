package com.batman.volleyproject;

/**
 * Created by HP on 4/28/2017.
 */

public class MovieItem {
    private String title , release_date , poster_path ;
    private int id;
    private double vote_average;

    public MovieItem(String title, String release_date, String poster_path, int id, double vote_average) {
        this.title = title;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.id = id;
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
}
