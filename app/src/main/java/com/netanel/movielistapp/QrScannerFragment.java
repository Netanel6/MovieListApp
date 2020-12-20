package com.netanel.movielistapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.netanel.movielistapp.pojo.Movie;
import com.netanel.movielistapp.room.MovieDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        cameraPermission(view);
    }


    private void cameraPermission(View view) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.CAMERA
                    }, TAKE_PICTURE_REQUEST);
        } else {
            openQrScanner(view);
        }
    }

    private void openQrScanner(View view) {
        codeScannerView = view.findViewById(R.id.qr_scanner);
        codeScanner = new CodeScanner(getActivity(), codeScannerView);
        codeScanner.startPreview();

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                QrScannerFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJsonToObject(result);

                    }
                });
            }
        });
    }

    private void parseJsonToObject(Result result) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            String title = obj.getString("title");
            String image = obj.getString("image");
            int rating = obj.getInt("rating");
            int releaseYear = obj.getInt("releaseYear");
            ArrayList<String> genreListData = new ArrayList<String>();
            JSONArray jArray = obj.getJSONArray("genre");
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    genreListData.add(jArray.getString(i));
                }
            }
            ArrayList<String> genre = genreListData;
            Movie movie = new Movie(title, image, rating, releaseYear, genre);

            if (QrScannerFragment.movieDatabase.movieDao().isDataExist(title) == 0) {
                QrScannerFragment.movieDatabase.movieDao().insert(movie);

                Snackbar snackbar = Snackbar.make(getView(), title + " added to the list", Snackbar.LENGTH_LONG);
                View snackbarLayout = snackbar.getView();
                TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite, 0, 0, 0);
                snackbar.show();
//                Snackbar.make(getView(), title + " added to the list", BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                Snackbar snackbar = Snackbar.make(getView(), title + " is already in the list", Snackbar.LENGTH_LONG);
                View snackbarLayout = snackbar.getView();
                TextView textView = (TextView)snackbarLayout.findViewById(R.id.snackbar_text);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error, 0, 0, 0);
                snackbar.show();
//                Snackbar.make(getView(), title + " is already in the list!", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}