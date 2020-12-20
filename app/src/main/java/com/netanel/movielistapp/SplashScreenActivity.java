package com.netanel.movielistapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.netanel.movielistapp.pojo.Movie;
import com.netanel.movielistapp.room.MovieDatabase;
import com.netanel.movielistapp.utils.JsonPlaceHolderApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView splashScreenIcon;
     static MovieDatabase movieDatabase;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Initiate the Room database
        movieDatabase = Room.databaseBuilder(getApplicationContext(), MovieDatabase.class, "moviedb").allowMainThreadQueries().build();
        setUpViews();
        getMovies();
    }

    private void setUpViews() {
        splashScreenIcon = findViewById(R.id.splash_screen_icon);
    }

    /*
    Using Retrofit library the url is translated to ArrayList and extract the data using objects
    then a statement says if the data is exist do not save it again if not save it
    the saving is happening only in the first app loading
     */
    private void getMovies() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.androidhive.info/json/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<List<Movie>> call = jsonPlaceHolderApi.getMovies();
        call.enqueue(new Callback<List<Movie>>() {

            @Override
            public void onResponse(Call<List<Movie>> call, retrofit2.Response<List<Movie>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(SplashScreenActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Movie> movies = response.body();
                if (SplashScreenActivity.movieDatabase.movieDao().isDataExist("Dawn of the Planet of the Apes") == 0) {
                    pd = new ProgressDialog(SplashScreenActivity.this);
                    pd.setMessage("Please wait");
                    pd.setCancelable(false);
                    pd.show();
                    for (Movie movie : movies) {
                        Movie newMovie = new Movie(movie.getTitle(), movie.getImage(), movie.getRating(), movie.getReleaseYear(), movie.getGenre());
                        SplashScreenActivity.movieDatabase.movieDao().insert(newMovie);
                    }
                } else {
                    setUpAnim();
                }
                setUpAnim();
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Toast.makeText(SplashScreenActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //fade animation
    private void setUpAnim() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Animation aniFade = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.fade_out);
            splashScreenIcon.startAnimation(aniFade);
            handler.postDelayed(() -> {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                finish();
            }, 1970);
            splashScreenIcon.setVisibility(View.INVISIBLE);
        }, 2000);
    }
}