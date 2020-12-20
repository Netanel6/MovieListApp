package com.netanel.movielistapp.room;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.netanel.movielistapp.utils.Converters;
import com.netanel.movielistapp.pojo.Movie;

//AppDatabase
@Database(entities = {Movie.class}, version = 1 , exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();
   }
