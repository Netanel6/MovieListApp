package com.netanel.movielistapp.movie_recycler_view;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netanel.movielistapp.R;
import com.netanel.movielistapp.pojo.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    List<Movie> movieList;
    public static OnItemClick onItemClick;

    //Empty constructor
    public MovieListAdapter() {
    }

    //Constructor with ArrayList
    public MovieListAdapter(List<Movie> movieList) {
        this.movieList = movieList;
    }

    //movie_single_cell inflation
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_single_cell, parent, false);
        return new ViewHolder(view);
    }

    //bind the model data with the UI
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTv.setText(movie.getTitle());
        Picasso.get().load(movie.getImage()).into(holder.movieIv);
        holder.itemView.setOnClickListener(view -> {
            //catch the adapters' position with the specific model data
            if (onItemClick != null && position != RecyclerView.NO_POSITION) {
                onItemClick.getItem(movie, position);
            }
        });
    }

    //getItem count the size the same as the array list size
    //if null returns 0 if not returns the list size
    @Override
    public int getItemCount() {
       try {
           return movieList.size();
       }catch (Exception e){
           Log.d(TAG, "getItemCount: " + e.getMessage());
           return 0;
       }
    }

    //initiate the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView movieTv;
        ImageView movieIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieTv = itemView.findViewById(R.id.movie_tv);
            movieIv = itemView.findViewById(R.id.movie_iv);
        }
    }

    //OnItemClick interface for clicking on the item action
    public interface OnItemClick {
        void getItem(Movie movie, int position);
    }

    //set the click the same as adapter
    public void setOnItemClick(OnItemClick onitemClick) {
        MovieListAdapter.onItemClick = onitemClick;
    }
}
