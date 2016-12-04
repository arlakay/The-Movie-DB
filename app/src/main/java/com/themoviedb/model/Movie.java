package com.themoviedb.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    @SerializedName("poster_path")
    public final String posterPath;
    @SerializedName("overview")
    public final String overview;
    @SerializedName("release_date")
    public final String releaseDate;
    @SerializedName("id")
    public final long id;
    @SerializedName("original_title")
    public final String originalTitle;
    @SerializedName("title")
    public final String title;
    @SerializedName("popularity")
    public final double popularity;
    @SerializedName("vote_count")
    public final int voteCount;
    @SerializedName("vote_average")
    public final double voteAverage;
    public final String tagline;

    public Movie(String posterPath, String overview, String releaseDate, long id,
                 String originalTitle, String title, double popularity, int voteCount,
                 int voteAverage, String tagline) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.tagline = tagline;
    }
}
