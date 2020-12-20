package com.netanel.movielistapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.netanel.movielistapp.pojo.Movie;
import com.netanel.movielistapp.room.MovieDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class QrScannerFragment extends Fragment {

    static MovieDatabase movieDatabase;
    private static final int TAKE_PICTURE_REQUEST = 1;
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    public QrScannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initiate the Room database
        movieDatabase = Room.databaseBuilder(getActivity(), MovieDatabase.class, "moviedb").allowMainThreadQueries().build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        codeScannerView = view.findViewById(R.id.qr_scanner);

    }

    @Override
    public void onResume() {
        super.onResume();
        cameraPermission();

    }

    private void cameraPermission() {
        //Check if the user gave camera permission and if not the camera will not open
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    }, TAKE_PICTURE_REQUEST);
            new QrScannerFragment();
        } else {
            openQrScanner();
        }
    }

    //Open the qr scanner along with the camera using Budiyev qr scanner library
    private void openQrScanner() {
        codeScanner = new CodeScanner(getActivity(), codeScannerView);
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                QrScannerFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            parseJsonToObject(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //Get the data from the qr scanner and parse it from json and save it to the database
    private void parseJsonToObject(Result result) throws IOException {
        JSONObject obj = null;
        try {
            obj = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            String title = obj.getString("title");
            String imageUrl = obj.getString("image");
            int rating = obj.getInt("rating");
            int releaseYear = obj.getInt("releaseYear");
            ArrayList<String> genreListData = new ArrayList<>();
            JSONArray jArray = obj.getJSONArray("genre");
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    genreListData.add(jArray.getString(i));
                }
            }
            // (Start) <img src=" , (End)  " srcset
            //full url https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Document document = Jsoup.connect(imageUrl).get();
                String tags = document.toString();
                int start = tags.indexOf("<img src=\"")+10;
                int end = tags.indexOf("\" srcset", start);

                String src = tags.substring(start, end);
                Log.d("Document", "parseJsonToObject: " + src);

                Movie movie = new Movie(title, src, rating, releaseYear, genreListData);
                //cant parse html to image! and it wont show as an image in the recycler

                //if the data isnt exist add it to the database else show a snackbar saying the data is already exist
                if (QrScannerFragment.movieDatabase.movieDao().isDataExist(title) == 0) {
                    QrScannerFragment.movieDatabase.movieDao().insert(movie);

                    Snackbar snackbar = Snackbar.make(getView(), title + " added to the list", Snackbar.LENGTH_LONG);
                    View snackbarLayout = snackbar.getView();
                    TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite, 0, 0, 0);
                    snackbar.show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(getView(), title + " is already in the list", Snackbar.LENGTH_LONG);
                    View snackbarLayout = snackbar.getView();
                    TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
                    snackbar.show();
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}