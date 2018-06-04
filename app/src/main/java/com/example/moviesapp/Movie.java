package com.example.moviesapp;

import android.os.Parcel;
import android.os.Parcelable;


public class Movie implements Parcelable {


    public static final Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    String filmTitle;
    String releaseDate;
    String filmPoster;
    double voteAverage;
    String plotSynopsis;
    String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185";

    //Constructor
    public Movie(int position) {
    }


    //Constructor
    public Movie(String filmTitle, String releaseDate, String filmPoster, double voteAverage, String plotSynopsis) {
        this.filmTitle = filmTitle;
        this.releaseDate = releaseDate;
        this.filmPoster = filmPoster;
        this.voteAverage = voteAverage;
        this.plotSynopsis = plotSynopsis;
    }


    public Movie(Parcel in) {
        filmTitle = in.readString();
        releaseDate = in.readString();
        filmPoster = in.readString();
        voteAverage = in.readDouble();
        plotSynopsis = in.readString();

    }

    public String getFilmTitle() {
        return this.filmTitle;
    }

    public void setFilmTitle() {
        this.filmTitle = filmTitle;

    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate() {
        this.releaseDate = releaseDate;

    }

    public String getFilmPoster() {
        return MOVIE_IMAGE_BASE_URL + this.filmPoster;
    }

    public void setFilmPoster() {
        this.filmPoster = filmPoster;

    }

    public double getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage() {
        this.voteAverage = voteAverage;

    }

    public String getPlotSynopsis() {
        return this.plotSynopsis;
    }

    public void setPlotSynopsis() {
        this.plotSynopsis = plotSynopsis;

    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filmTitle);
        dest.writeString(releaseDate);
        dest.writeString(filmPoster);
        dest.writeDouble(voteAverage);
        dest.writeString(plotSynopsis);
    }

    public int describeContents() {
        return 0;
    }
}
