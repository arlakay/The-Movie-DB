package com.themoviedb.ui.list;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.themoviedb.R;
import com.themoviedb.api.MovieService;
import com.themoviedb.api.RestApi;
import com.themoviedb.api.ServiceConfig;
import com.themoviedb.model.Movie;
import com.themoviedb.model.MoviesResponse;
import com.themoviedb.ui.detail.DetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ERD on 12/3/2016.
 */

public class MainFragment extends Fragment implements SortableFragment {
    private final String TAG = MainFragment.class.getSimpleName();
    public static final int SPAN_COUNT = 3;
    private static final String DEFAULT_SORT = "popular";
    public static final String KEY_SORT = "key_sort";

    @BindView(R.id.recyclerview)RecyclerView recyclerView;
    @BindView(R.id.loading)ProgressBar loadingView;

    private MoviesAdapter adapter;
    private String sort;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT,
                LinearLayoutManager.VERTICAL,
                false);

        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new MoviesAdapter();

        adapter.setOnClickListener(new MoviesAdapter
                .OnClickListener() {
            @Override
            public void onItemClickListener(long id,
                                            Movie movie) {
                openMovieDetail(id, movie);
            }
        });

        recyclerView.setAdapter(adapter);

        if (savedInstanceState == null) {
            sort = DEFAULT_SORT;
        } else {
            sort = savedInstanceState.getString(KEY_SORT);
        }

        getListMovie(sort);

    }

    private void openMovieDetail(long id, Movie movie) {
        String movieJson = new Gson().toJson(movie);

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("movie_id", id);
        intent.putExtra("movie_data", movieJson);
        getActivity().startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_SORT, sort);
        super.onSaveInstanceState(outState);
    }

    private void getListMovie(String sort) {
        loadingView.setVisibility(View.VISIBLE);

        MovieService apiService =
                RestApi.getClient().create(MovieService.class);

        Call<MoviesResponse> call = apiService.listOfMovie(sort, ServiceConfig.API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                loadingView.setVisibility(View.GONE);

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
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                loadingView.setVisibility(View.GONE);

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


    @Override
    public void sortMovie(String sort) {
        this.sort = sort;
        getListMovie(sort);
    }
}
