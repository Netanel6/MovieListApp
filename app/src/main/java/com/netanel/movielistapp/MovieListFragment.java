package com.netanel.movielistapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.netanel.movielistapp.movie_recycler_view.MovieListAdapter;
import com.netanel.movielistapp.pojo.Movie;
import com.netanel.movielistapp.room.MovieDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListFragment extends Fragment {
    static MovieDatabase movieDatabase;
    static RecyclerView recyclerView;
    static MovieListAdapter movieListAdapter = new MovieListAdapter();
    private BottomSheetBehavior mBottomSheetBehavior;

    //Required empty constructor
    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initiation of the Room database
        movieDatabase = Room.databaseBuilder(getActivity(), MovieDatabase.class, "moviedb").allowMainThreadQueries().build();
    }

    //Inflation of the fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        getData();
        setSelectMovieInfo(view);
    }

    //Get data from the database as Objects and attach them to the recycler view
    private static void getData() {
        class GetData extends AsyncTask<Void, Void, List<Movie>> {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> allMovies = MovieListFragment.movieDatabase.movieDao().getAllMovies();
                return allMovies;
            }

            @Override
            protected void onPostExecute(List<Movie> myDataList) {
                movieListAdapter = new MovieListAdapter(myDataList);
                recyclerView.setAdapter(movieListAdapter);
                movieListAdapter.notifyDataSetChanged();
                super.onPostExecute(myDataList);
            }
        }
        GetData gd = new GetData();
        gd.execute();
    }

    /*
    Initiate the bottom sheet when OnClickItem interface is detecting a single click on the specific
    item in the recycler view
     */

    private void setSelectMovieInfo(View view) {
        LinearLayout mCustomBottomSheet = view.findViewById(R.id.custom_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mCustomBottomSheet);

        movieListAdapter.setOnItemClick(new MovieListAdapter.OnItemClick() {
            @Override
            public void getItem(Movie movie, int position) {
                //Checks the state of the bottom sheet
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                //Add callbacck to the bottom sheet
                mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        TextView bottomSheetTitle, bottomSheetRating, bottomSheetReleaseYear, bottomSheetGenre;
                        ImageView bottomSheetImage;

                        //Initiate the views when the bottom sheet state == to expended
                        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                            bottomSheetTitle = view.findViewById(R.id.movie_title_bottom_sheet);
                            bottomSheetRating = view.findViewById(R.id.movie_rating_bottom_sheet);
                            bottomSheetReleaseYear = view.findViewById(R.id.movie_release_year_bottom_sheet);
                            bottomSheetGenre = view.findViewById(R.id.movie_genre_bottom_sheet);
                            bottomSheetImage = view.findViewById(R.id.movie_bottom_sheet_image);
                            bottomSheetTitle.setText(movie.getTitle());
                            bottomSheetRating.setText(String.valueOf(movie.getRating()));
                            bottomSheetReleaseYear.setText(String.valueOf(movie.getReleaseYear()));
                            //String builder to display the array list of genre as one by one strings
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < movie.getGenre().size(); i++) {
                                builder.append(movie.getGenre().get(i));
                                builder.append(" ");
                                builder.append(("\u2022"));
                                builder.append(" ");
                            }
                            bottomSheetGenre.setText(builder.toString());
                            //Load the image url as image view using Picasso library
                            Picasso.get().load(movie.getImage()).fit().into(bottomSheetImage);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
            }
        });
    }
}