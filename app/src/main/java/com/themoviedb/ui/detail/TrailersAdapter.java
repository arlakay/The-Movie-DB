package com.themoviedb.ui.detail;

import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.themoviedb.R;
import com.themoviedb.api.ServiceConfig;
import com.themoviedb.model.Trailer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Trailer> trailers = new ArrayList<>();
    private OnClickListener onClickListener;

    public void setMoviesData(List<Trailer> movies) {
        this.trailers.clear();
        this.trailers.addAll(movies);
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

            Uri uri = Uri.parse(ServiceConfig.BASE_VIDEO_URL + trailers.get(position).key);

            movieViewHolder.bind(uri.toString(), trailers.get(position).name);
            movieViewHolder.setItemClickListener(onClickListener, trailers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(@LayoutRes int resId, ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        }
    }

    public class MovieViewHolder extends Holder {
        @BindView(R.id.txt_name)
        TextView name;

        public MovieViewHolder(ViewGroup parent) {
            super(R.layout.list_item_trailers, parent);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String posterUrl, String releaseDate) {
            name.setText(releaseDate);
        }


        public void setItemClickListener(final OnClickListener onClickListener, final Trailer movie) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClickListener(movie.key, movie);
                }
            });
        }
    }

    public interface OnClickListener {
        void onItemClickListener(String key, Trailer movie);
    }
}
