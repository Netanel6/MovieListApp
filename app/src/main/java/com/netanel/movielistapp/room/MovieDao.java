package com.netanel.movielistapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.netanel.movielistapp.pojo.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movie_table ORDER BY releaseYear DESC ")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movie_table WHERE title = :userName")
    int isDataExist(String userName);


}

