package com.netanel.movielistapp.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.netanel.movielistapp.utils.Converters;
import com.netanel.movielistapp.pojo.Movie;

@Database(entities = {Movie.class}, version = 1 , exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase instance;
    public abstract MovieDao movieDao();

    public static synchronized MovieDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MovieDatabase.class, "movie_database").
                    fallbackToDestructiveMigration().build();
        }
        return instance;
    }

}
