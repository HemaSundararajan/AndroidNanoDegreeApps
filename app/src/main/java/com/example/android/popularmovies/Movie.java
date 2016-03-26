package com.example.android.popularmovies;

/**
 * Created by hema on 27/2/16.
 */
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Movie implements Serializable {
    private int movieId;
    private String movieImageUrl;
    private String originalTitle;
    private String synopsis;
    private Double userRating;
    private String releaseDate;
    private Boolean isFavourite;
    private List<String> reviews;
    private List<String> trailerUrl;

    public Movie() {

    }
    public Movie(int MovieId,String MovieImageUrl, String OriginalTitle,String Synopsis,Double UserRating,String ReleaseDate,Boolean isFavourite) {
        this.movieId = MovieId;
        this.movieImageUrl = MovieImageUrl;
        this.originalTitle = OriginalTitle;
        this.synopsis = Synopsis;
        this.userRating = UserRating;
        this.releaseDate = ReleaseDate;
        this.isFavourite = isFavourite;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int MovieId) {
        this.movieId = MovieId;
    }

    public String getMovieImageUrl() {
        return movieImageUrl;
    }

    public void setMovieImageUrl(String MovieImageUrl) {
        this.movieImageUrl = MovieImageUrl;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String OriginalTitle) {
        this.originalTitle = OriginalTitle;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String Synopsis) {
        this.synopsis = Synopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public void setUserRating(double UserRating){
        this.userRating = UserRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String ReleaseDate){
        this.releaseDate = ReleaseDate;
    }

    public boolean getIsFavourite() { return isFavourite; }

    public void setIsFavourite(Boolean isFavourite) { this.isFavourite = isFavourite;}

    public List<String> getReviews() { return reviews; }

    public void setReviews(List<String> reviews) { this.reviews = reviews;}

    public List<String> getTrailerUrl() {return this.trailerUrl;}

    public void setTrailerUrl(List<String> trailerUrl) {this.trailerUrl = trailerUrl;}
}

