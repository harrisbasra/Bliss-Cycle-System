package com.cyclesystem.bliss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cyclesystem.bliss.databinding.ActivityPreBookingMapsBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cyclesystem.bliss.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class PreBookingMaps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityPreBookingMapsBinding binding;

    double DecidingLatitude = -1;
    double DecidingLongitude = -1;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        // I suppressed the missing-permission warning because this wouldn't be executed in my
        // case without location services being enabled
        @SuppressLint("MissingPermission") android.location.Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        double userLat = 0.0;
        double userLong = 0.0;
        super.onCreate(savedInstanceState);

        binding = ActivityPreBookingMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.bance.setOnClickListener(this::geoLocate);
        binding.bne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DecidingLatitude==-1 && DecidingLongitude==-1){
                    Snackbar.make(v, "Search A Place First", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else{
                    mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(@NonNull Location location) {
                            double Lat = location.getLatitude();
                            double Lon = location.getLongitude();

                            double dLat = Math.toRadians(DecidingLatitude - Lat);
                            double dLon = Math.toRadians(DecidingLongitude - Lon);
                            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                                    Math.cos(Math.toRadians(Lat)) * Math.cos(Math.toRadians(DecidingLatitude)) *
                                            Math.sin(dLon/2) * Math.sin(dLon/2);
                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                            double distance = 6371 * c;

                            FileOutputStream fos3 = null;
                            String W = String.valueOf(distance);
                            try {
                                fos3 = openFileOutput("endfile.txt", Context.MODE_PRIVATE);
                                fos3.write(W.getBytes());
                                fos3.flush();
                                fos3.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                            Intent newIntent = new Intent(PreBookingMaps.this, EndResults.class);
                            startActivity(newIntent);

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }
    private void geoLocate(View view) {
        String locationName = binding.beaunce.getText().toString();
        Geocoder geocoder = new Geocoder(  this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 3);
            if(addressList.size()>0) {
                Address address = addressList.get(0);
                gotoLocation(address.getLatitude(), address.getLongitude());
                DecidingLatitude = address.getLatitude();
                DecidingLongitude = address.getLongitude();
                mMap.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())));
            }

        }
        catch (IOException e){

        }
    }
    private void gotoLocation(double latitude, double longitude) {
        LatLng LatLng = new LatLng(latitude, longitude);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng, 18);
        mMap.moveCamera(cameraUpdate);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    }



