package com.example.hw2.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.hw2.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Map_Fragment extends Fragment implements OnMapReadyCallback {

    private AppCompatActivity activity;
    private MapView scores_MAP_map;
    private final String KEY ="AIzaSyCHWCvuGCDaFuHClRuEh4ajEYVZQCIvHrc";
    private GoogleMap map;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        findViews(view);
        initGoogleMap(savedInstanceState);

        return view;
    }


    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(KEY);
        }

        scores_MAP_map.onCreate(mapViewBundle);
        scores_MAP_map.getMapAsync(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        scores_MAP_map.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        scores_MAP_map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        scores_MAP_map.onStop();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        this.map = map;
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void onPause() {
        scores_MAP_map.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        scores_MAP_map.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        scores_MAP_map.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(KEY, mapViewBundle);
        }
    }
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if (context instanceof AppCompatActivity){
            activity = (AppCompatActivity) context;
        }
    }

    private void findViews(View view) {
        scores_MAP_map = view.findViewById(R.id.scores_MAP_map);
    }



    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public Marker setMarker(double lat, double lon) {
        Log.d("Map_Fragment", "buttonRecordClicked lat: "+ lat + ", lon: " + lon);
        if(map != null){
            map.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Marker"));
        }
        return null;
    }
}