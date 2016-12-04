package com.themoviedb.api;

import com.themoviedb.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieService {

    @GET("movie/{sort}")
    Call<MoviesResponse> listOfMovie(@Path("sort") String sort,
                                     @Query("api_key") String apiKey);

    //Trailers
//    @GET("movie/{id}/videos")
//    Call<TrailersResponse> trailerMovie(@Path("id") long id, @Query("api_key") String apiKey);
    //Reviews
//    @GET("movie/{id}/reviews")
//    Call<ReviewsResponse> reviewsMovie(@Path("id") long id, @Query("api_key") String apiKey);

}
