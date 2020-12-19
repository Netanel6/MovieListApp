package com.netanel.movielistapp.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.netanel.movielistapp.pojo.Movie;

import java.util.List;

public class MovieRepo {

    private MovieDao movieDao;
    private List<Movie> allMovies;

    public MovieRepo(Application application){
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        movieDao = movieDatabase.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public void insert(Movie movie){
        new InsertMovieAsyncTask(movieDao).execute(movie);
    }

    public void delete(Movie movie){
        new DeleteMovieAsyncTask(movieDao).execute(movie);
    }

    public List<Movie> getAllMovies(){
        return allMovies;
    }

    public static class InsertMovieAsyncTask extends AsyncTask<Movie, Void, Void>{
        private MovieDao movieDao;

        public InsertMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
         movieDao.insert(movies[0]);
            return null;
        }
    }

    public static class DeleteMovieAsyncTask extends AsyncTask<Movie, Void, Void>{
        private MovieDao movieDao;

        public DeleteMovieAsyncTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }
        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.delete(movies[0]);
            return null;
        }
    }
}
