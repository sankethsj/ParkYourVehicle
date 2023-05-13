package com.sankethjain.parkyourvehicle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView txtLat, textView, msg;
    String lat, log;
    Button parkHere, findMyVehicle, delete, share, buttonOK;
    Activity a;
    private AdView mAdView;
    private CardView adCard;


    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        adCard = findViewById(R.id.adcard);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                adCard.setVisibility(View.VISIBLE);
            }
        });

        txtLat = findViewById(R.id.textview1);
        textView = findViewById(R.id.textview2);
        msg = findViewById(R.id.msg);
        parkHere = findViewById(R.id.button);
        findMyVehicle = findViewById(R.id.button1);
        delete = findViewById(R.id.delete);
        share = findViewById(R.id.share);
        buttonOK = findViewById(R.id.buttonOk);


        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        if(!getMsg().isEmpty()){
            parkHere.setEnabled(false);
            msg.setText(getMsg());
        }else{
            msg.setText("Your Vehicle Park Location is not saved");
            parkHere.setEnabled(true);
        }


        parkHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParkedLocation();
                msg.setText(getMsg());
                customDialog cdd=new customDialog(MainActivity.this);
                Objects.requireNonNull(cdd.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                parkHere.setEnabled(false);
            }

        });


        findMyVehicle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(!getLat().isEmpty() || !getLog().isEmpty()){
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+getLat()+","+getLog()+"(You parked here)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }else{
                    Toast.makeText(MainActivity.this, "You need Google Maps to navigate", Toast.LENGTH_SHORT).show();
                }
                } else{
                    Toast.makeText(MainActivity.this, "You have no park location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLocation();
                msg.setText("No locations found");
                parkHere.setEnabled(true);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getLat().isEmpty() || !getLog().isEmpty()) {
                    String url = "My Vehicle is parked here\n" + "https://www.google.com/maps/search/?api=1&query=" + getLat() + "," + getLog();
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    share.putExtra(Intent.EXTRA_TEXT, url);
                    startActivity(Intent.createChooser(share, "Vehicle Location"));
                }else{
                    Toast.makeText(MainActivity.this, "No locations to share", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    //saving location
    private void saveLocationDetails(String latitude, String longitude, String message) {
        SharedPreferences sharedPreferences = getSharedPreferences("LocationDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Latitude", latitude);
        editor.putString("Longitude", longitude);
        editor.putString("Message", message);
        editor.apply();
    }
    private String getLat() {
        SharedPreferences sharedPreferences = getSharedPreferences("LocationDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Latitude", "");
    }
    private String getLog() {
        SharedPreferences sharedPreferences = getSharedPreferences("LocationDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Longitude", "");
    }
    private String getMsg() {
        SharedPreferences sharedPreferences = getSharedPreferences("LocationDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Message", "");
    }
    private void deleteLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("LocationDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    //get access to location permission
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Permission Denied
                    Toast.makeText(this, "Please allow Location access to use this app", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //Get location
    @SuppressLint("SetTextI18n")
    private void getParkedLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location mylocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(mylocation!=null){
            lat = String.valueOf(mylocation.getLatitude());
            log = String.valueOf(mylocation.getLongitude());
            String message = "Your vehicle park location is \n\nLatitude : "+lat+"\nLongitude : "+log;
            saveLocationDetails(lat, log, message);
        }

    }


}