package com.themoviedb.ui.list;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.themoviedb.R;
import com.themoviedb.model.Movie;
import com.themoviedb.utils.ImageSize;
import com.themoviedb.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnClickListener onClickListener;

    public void setMoviesData(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

            Uri uri = ImageUtils.movieUrl(
                    ImageSize.w185.getValue(),
                    movies.get(position).posterPath);

            movieViewHolder.bind(uri.toString(), movies.get(position).releaseDate);
            movieViewHolder.setItemClickListener(onClickListener, movies.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(@LayoutRes int resId, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }
    }

    public class MovieViewHolder extends Holder {

        @BindView(R.id.poster) ImageView posterView;
        @BindView(R.id.release_date) TextView releaseDateView;

        public MovieViewHolder(ViewGroup parent) {
            super(R.layout.card_grid_movie, parent);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String posterUrl, String releaseDate) {
            Picasso.with(itemView.getContext()).load(posterUrl).into(posterView);
            releaseDateView.setText(releaseDate);
        }


        public void setItemClickListener(final OnClickListener onClickListener, final Movie movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClickListener(movie.id, movie);
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClickListener(long id, Movie movie);
    }
}
