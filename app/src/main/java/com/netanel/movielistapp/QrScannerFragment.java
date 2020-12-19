package com.netanel.movielistapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netanel.movielistapp.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import retrofit2.Retrofit;

public class QrScannerFragment extends Fragment {

    private static final int TAKE_PICTURE_REQUEST = 1;
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;

    public QrScannerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        codeScanner.setDecodeCallback(result -> {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(QrScannerFragment.this.getContext(), result.toString(), Toast.LENGTH_SHORT).show();



               /*     String title = jsonObject.getS("title").toString();
                    String image = jsonObject.get("image").toString();
                    int rating = jsonObject.get("rating").getAsInt();
                    int releaseYear = jsonObject.get("releaseYear").getAsInt();

                    Movie movie = new Movie(title, image, rating, releaseYear, new ArrayList<>() );*/
                }
            });
        });


    }
}