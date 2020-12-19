package com.netanel.movielistapp.movie_recycler_view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netanel.movielistapp.R;
import com.netanel.movielistapp.pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    List<Movie> movieList;
    public static OnItemClick onItemClick;

    public MovieListAdapter() {
    }

    public MovieListAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_single_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTv.setText(movie.getTitle());
        Picasso.get().load(movie.getImage()).into(holder.movieIv);
        holder.itemView.setOnClickListener(view -> {
            if (onItemClick != null && position != RecyclerView.NO_POSITION) {
                onItemClick.getItem(movie, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList == null) {
            return 0;
        } else {
            return movieList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTv;
        ImageView movieIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTv = itemView.findViewById(R.id.movie_tv);
            movieIv = itemView.findViewById(R.id.movie_iv);
        }
    }

    public interface OnItemClick {
        void getItem(Movie movie, int position);
    }

    public void setOnItemClick(OnItemClick onitemClick) {
        MovieListAdapter.onItemClick = onitemClick;
    }
}
