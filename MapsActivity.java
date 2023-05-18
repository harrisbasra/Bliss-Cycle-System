package com.cyclesystem.bliss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cyclesystem.bliss.databinding.ActivityMapsBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    double Total =0.00;
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



        FileOutputStream fos = null;
        String AAAA = String.valueOf(userLat);
        try {
            fos = openFileOutput("softec.txt", Context.MODE_PRIVATE);
            fos.write(AAAA.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos2 = null;
        String BBBB = String.valueOf(userLong);
        try {
            fos2 = openFileOutput("nascon.txt", Context.MODE_PRIVATE);
            fos2.write(BBBB.getBytes());
            fos2.flush();
            fos2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Button hjkl = (Button) findViewById(R.id.beaunce);
        hjkl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileOutputStream fos3 = null;
                String UUUU = String.valueOf(Total);
                try {
                    fos3 = openFileOutput("endfile.txt", Context.MODE_PRIVATE);
                    fos3.write(UUUU.getBytes());
                    fos3.flush();
                    fos3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }



                Intent newIntent = new Intent(MapsActivity.this, EndResults.class);
                startActivity(newIntent);
            }
        });

    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
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

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {



                double lon1 = location.getLongitude();
                double lat1 = location.getLatitude();
                String AAAA = "";
                String BBBB = "";
                try {
                    FileInputStream fin = openFileInput("softec.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while ((a = fin.read()) != -1) {
                        temp.append((char) a);
                    }
                    AAAA = temp.toString();
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileInputStream fin = openFileInput("nascon.txt");
                    int a;
                    StringBuilder temp = new StringBuilder();
                    while ((a = fin.read()) != -1) {
                        temp.append((char) a);
                    }
                    BBBB = temp.toString();
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!(AAAA.equals("")&&BBBB.equals(""))){
                    double lat2 = Double.parseDouble(AAAA);
                    double lon2 = Double.parseDouble(BBBB);


                    double theta = lon1 - lon2;
                    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
                    dist = Math.acos(dist);
                    dist = (dist * 180.0 / Math.PI);
                    dist = dist * 60 * 1.1515;
                    dist = dist * 1.609344;
                    dist = dist*1000;
                    if(dist<0){
                        dist=-1*dist;
                    }
                    Total = Total+dist;
                    TextView opi = (TextView) findViewById(R.id.bance);
                    opi.setText("Distance: "+Total+" m");
                }
                else{

                }
            }
        });
    }
}

