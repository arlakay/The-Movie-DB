package com.themoviedb.ui.detail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.themoviedb.R;
import com.themoviedb.api.MovieService;
import com.themoviedb.api.RestApi;
import com.themoviedb.api.ServiceConfig;
import com.themoviedb.model.Movie;
import com.themoviedb.model.Trailer;
import com.themoviedb.model.TrailersResponse;
import com.themoviedb.utils.ImageSize;
import com.themoviedb.utils.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ERD on 12/3/2016.
 */

public class DetailFragment extends Fragment {
    private final String TAG = DetailFragment.class.getSimpleName();
    public static final String ARG_MOVIE_ID = "movie_id";
    public static final String ARG_MOVIE_DATA = "movie_data";
    private TrailersAdapter adapter;

    @BindView(R.id.poster)ImageView posterView;
    @BindView(R.id.title)TextView titleView;
    @BindView(R.id.rating_average)TextView ratingAverageView;
    @BindView(R.id.rating_bar)RatingBar ratingBarView;
    @BindView(R.id.release_date)TextView releaseDateView;
    @BindView(R.id.rating_average2)TextView ratingAverageView2;
    @BindView(R.id.rating_bar2)RatingBar ratingBarView2;
    @BindView(R.id.recyclerview_trailer)RecyclerView recyclerViewTrailer;

    public static Fragment newInstance(long movieId, String movieData) {
        Bundle args = new Bundle();
        args.putLong(ARG_MOVIE_ID, movieId);
        args.putString(ARG_MOVIE_DATA, movieData);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Movie movie = new Gson().fromJson(getArguments().getString(ARG_MOVIE_DATA), Movie.class);

        Uri uri = ImageUtils.movieUrl(ImageSize.w342.getValue(), movie.posterPath);
        Picasso.with(getActivity()).load(uri.toString()).into(posterView);

        titleView.setText(movie.title);
        ratingAverageView.setText(String.valueOf(movie.voteAverage));
        ratingBarView.setRating((float) movie.voteAverage);
        ratingAverageView2.setText(String.valueOf(movie.voteAverage));
        ratingBarView2.setRating((float) movie.voteAverage);
        releaseDateView.setText(movie.releaseDate);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(
                getActivity(),
                LinearLayoutManager.VERTICAL,
                false);

        recyclerViewTrailer.setLayoutManager(gridLayoutManager);

        adapter = new TrailersAdapter();

        adapter.setOnClickListener(new TrailersAdapter.OnClickListener() {
            @Override
            public void onItemClickListener(String key,
                                            Trailer movie) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:" + key));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + key));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });

        recyclerViewTrailer.setAdapter(adapter);

        getTrailer(movie.id);

    }

    private void getTrailer(long id) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "loading...");

        MovieService apiService =
                RestApi.getClient().create(MovieService.class);

        Call<TrailersResponse> call = apiService.trailerMovie(id, ServiceConfig.API_KEY);
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse>call, Response<TrailersResponse> response) {
                dialog.dismiss();

                Log.e(TAG, "Status Code = " + response.code());
                Log.e(TAG, "Data received: " + new Gson().toJson(response.body().results));

                if (response.code() == 200 && response.isSuccessful()){
                    adapter.setMoviesData(response.body().results);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Symptom Tidak Ada");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<TrailersResponse>call, Throwable t) {
                dialog.dismiss();

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Kesalahan Jaringan");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });
    }

}
