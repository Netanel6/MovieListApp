package com.netanel.movielistapp.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.netanel.movielistapp.pojo.Movie;

import java.util.List;

/*By using DAOs to access my apps' database instead of query builders or direct queries,
I can preserve separation of concerns, a critical architectural principle.
DAOs also make it easier for me to mock database access when I test the app.*/
@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("SELECT * FROM movie_table ORDER BY releaseYear DESC ")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movie_table WHERE title = :userName")
    int isDataExist(String userName);


}

